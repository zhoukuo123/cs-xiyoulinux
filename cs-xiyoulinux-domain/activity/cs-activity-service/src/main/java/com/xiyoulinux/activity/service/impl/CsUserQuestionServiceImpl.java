package com.xiyoulinux.activity.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.vo.PageQuestionInfo;
import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.activity.inter.InterService;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.pojo.CsUserQuestion;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.vo.CsUserQuestionVo;
import com.xiyoulinux.enums.ActivityStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
public class CsUserQuestionServiceImpl implements ICsUserQuestionService {

    @Resource
    private CsUserQuestionMapper csUserQuestionMapper;

    @Resource
    private CsUserActivityMapper csUserActivityMapper;

    @Resource
    private InterService interService;

    @Override
    public PageQuestionInfo getPageUnresolvedIssues(int page) {
        return getPageQuestionInfo(page, ActivityStatus.UNRESOLVED);
    }

    @Override
    public PageQuestionInfo getPageResolvedIssues(int page) {
        return getPageQuestionInfo(page, ActivityStatus.RESOLVED);
    }

    @Override
    public void updateQuestionStatus(String id, ActivityStatus questionStatus) {
        csUserActivityMapper.updateQuestionStatus(id, questionStatus);
        csUserQuestionMapper.updateQuestionStatus(id, questionStatus);

    }

    private List<CsUserQuestionVo> getCsQuestionVo(List<CsUserQuestion> questionList) {
        if (null == questionList || questionList.size() == 0) {
            return null;
        }
        Set<String> idList = questionList.stream().map(CsUserQuestion::getUserId).collect(Collectors.toSet());
        log.info("Get question --- get userId [{}]", idList);

        List<String> questionIdList = questionList.stream().map(CsUserQuestion::getQuestionId).collect(Collectors.toList());
        log.info("Get question --- get questionId [{}]", questionIdList);

        log.info("Get question --- get userInfo....");
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);

        log.info("Get question --- get files....");
        Map<String, List<String>> fileUrlByActivityIdMap = interService.interCallFile(questionIdList);

        log.info("Get question --- get comment....");
        Map<String, Long> commentMap = interService.interCallComment(questionIdList);

        List<CsUserQuestionVo> csUserQuestionVos = new ArrayList<>();
        questionList.forEach(csUserQuestion -> {
            CsUserQuestionVo csUserQuestionVo = new CsUserQuestionVo();
            CsUserQuestionVo.Question simpleQuestion = CsUserQuestionVo.Question.to(csUserQuestion);
            csUserQuestionVo.setCsUserQuestion(simpleQuestion);
            csUserQuestionVo.setCsUserInfo(userMap == null ? null : userMap.get(csUserQuestion.getUserId()));
            csUserQuestionVo.setQuestionPicturesUrl(fileUrlByActivityIdMap == null ?
                    null : fileUrlByActivityIdMap.get(csUserQuestion.getQuestionId()));
            csUserQuestionVo.setCommentNumber(commentMap == null ? null : commentMap.get(csUserQuestion.getQuestionId()));
            csUserQuestionVos.add(csUserQuestionVo);
        });
        return csUserQuestionVos;
    }

    private PageQuestionInfo getPageQuestionInfo(int page, ActivityStatus activityStatus) {
        Page<CsUserQuestion> questionPage = new Page<>(page, 20);
        IPage<CsUserQuestion> pageIssues = null;
        if (activityStatus.code.equals(ActivityStatus.RESOLVED.code)) {
            pageIssues = csUserQuestionMapper.getPageResolvedIssues(questionPage);
        } else {
            pageIssues = csUserQuestionMapper.getPageUnresolvedIssues(questionPage);
        }
        List<CsUserQuestion> questions = pageIssues.getRecords();
        log.info("Get {} question -- page [{}] -- [{}]", activityStatus.description, page, questions);
        PageQuestionInfo pageQuestionInfo = new PageQuestionInfo();
        pageQuestionInfo.setQuestionInfos(getCsQuestionVo(questions));
        pageQuestionInfo.setHasMore(pageIssues.getPages() > page);
        return pageQuestionInfo;
    }

}




