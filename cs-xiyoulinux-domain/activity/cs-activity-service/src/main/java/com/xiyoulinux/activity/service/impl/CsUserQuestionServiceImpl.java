package com.xiyoulinux.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.vo.PageQuestionInfo;
import com.xiyoulinux.activity.vo.QuestionNumber;
import com.xiyoulinux.activity.inter.InterService;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.entity.CsUserQuestion;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.vo.CsUserQuestionVo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.enums.ActivityStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
public class CsUserQuestionServiceImpl implements ICsUserQuestionService {

    private final CsUserQuestionMapper csUserQuestionMapper;

    private final CsUserActivityMapper csUserActivityMapper;

    private final InterService interService;

    public CsUserQuestionServiceImpl(CsUserQuestionMapper csUserQuestionMapper,
                                     CsUserActivityMapper csUserActivityMapper,
                                     InterService interService) {
        this.csUserQuestionMapper = csUserQuestionMapper;
        this.csUserActivityMapper = csUserActivityMapper;
        this.interService = interService;
    }

    @Override
    public PageQuestionInfo getPageUnresolvedIssues(int page, String userId) {
        return getPageQuestionInfo(page, ActivityStatus.UNRESOLVED, userId);
    }

    @Override
    public PageQuestionInfo getPageResolvedIssues(int page, String userId) {
        return getPageQuestionInfo(page, ActivityStatus.RESOLVED, userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateQuestionStatus(String id, ActivityStatus questionStatus) {
        csUserActivityMapper.updateQuestionStatus(id, questionStatus);
        csUserQuestionMapper.updateQuestionStatus(id, questionStatus);
    }

    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getQuestionNumberFallback",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500"),
            }
    )
    @Override
    public QuestionNumber getQuestionNumber() {
        Integer unResolvedNumber = csUserQuestionMapper.selectCount(new QueryWrapper<CsUserQuestion>().eq("question_status", 1));
        Integer resolvedNumber = csUserQuestionMapper.selectCount(new QueryWrapper<CsUserQuestion>().eq("question_status", 0));
        log.info("find question number from db, unResolved [{}]、resolved [{}]", unResolvedNumber, resolvedNumber);
        //返回
        return new QuestionNumber(unResolvedNumber, resolvedNumber);
    }

    //前端搞一下降级显示
    public QuestionNumber getQuestionNumberFallback() {
        log.error("get question number into fallback method");
        return new QuestionNumber(-1, -1);
    }


    private List<CsUserQuestionVo> getCsQuestionVo(List<CsUserQuestion> questionList, String userId) {
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
            csUserQuestionVo.setCsUserInfo(userMap.get(csUserQuestion.getUserId()));
            csUserQuestionVo.setQuestionPicturesUrl(fileUrlByActivityIdMap.get(csUserQuestion.getQuestionId()));
            csUserQuestionVo.setCommentNumber(commentMap.get(csUserQuestion.getId()));
            csUserQuestionVo.setIsModify(userId.equals(csUserQuestion.getUserId()));
            csUserQuestionVos.add(csUserQuestionVo);
        });
        return csUserQuestionVos;
    }

    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageQuestionInfoFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500")
            }

    )
    private PageQuestionInfo getPageQuestionInfo(int page, ActivityStatus activityStatus, String userId) {
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
        pageQuestionInfo.setQuestionInfos(getCsQuestionVo(questions, userId));
        pageQuestionInfo.setHasMore(pageIssues.getPages() > page);
        return pageQuestionInfo;
    }

    private PageQuestionInfo getPageQuestionInfoFallBack(int page, ActivityStatus activityStatus, String userId) {
        log.error("user [{}] get page [{}] [{}] question into fallback method", userId, page, activityStatus.description);
        return null;
    }
}




