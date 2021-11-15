package com.xiyoulinux.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.bo.CsUserQuestionUpdateBo;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.vo.PageQuestionInfo;
import com.xiyoulinux.activity.vo.QuestionNumber;
import com.xiyoulinux.activity.inter.IntelService;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.entity.CsUserQuestion;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.vo.CsUserQuestionVo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.common.PageInfo;
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

    private final IntelService interService;

    public CsUserQuestionServiceImpl(CsUserQuestionMapper csUserQuestionMapper,
                                     CsUserActivityMapper csUserActivityMapper,
                                     IntelService interService) {
        this.csUserQuestionMapper = csUserQuestionMapper;
        this.csUserActivityMapper = csUserActivityMapper;
        this.interService = interService;
    }

    @Override
    public PageQuestionInfo getPageUnresolvedIssues(PageInfo pageInfo, String userId) {
        return getPageQuestionInfo(pageInfo, ActivityStatus.UNRESOLVED, userId);
    }

    @Override
    public PageQuestionInfo getPageResolvedIssues(PageInfo pageInfo, String userId) {
        return getPageQuestionInfo(pageInfo, ActivityStatus.RESOLVED, userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateQuestionStatus(CsUserQuestionUpdateBo csUserQuestionUpdateBo) {
        csUserActivityMapper.updateQuestionStatus(
                csUserQuestionUpdateBo.getQuestionId(),
                csUserQuestionUpdateBo.getQuestionStatus());
        csUserQuestionMapper.updateQuestionStatus(
                csUserQuestionUpdateBo.getQuestionId(),
                csUserQuestionUpdateBo.getQuestionStatus());
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
                            value = "3000"),
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
    public QuestionNumber getQuestionNumberFallback(Throwable throwable) {
        log.error("get question number into fallback method : [{}]",throwable.getMessage());
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


        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);
        log.info("Get question --- get userInfo success");


        Map<String, Long> commentMap = interService.interCallComment(questionIdList);
        log.info("Get question --- get comment success");

        List<CsUserQuestionVo> csUserQuestionVos = new ArrayList<>();
        questionList.forEach(csUserQuestion -> {
            CsUserQuestionVo csUserQuestionVo = new CsUserQuestionVo();
            CsUserQuestionVo.Question simpleQuestion = CsUserQuestionVo.Question.to(csUserQuestion);
            csUserQuestionVo.setCsUserQuestion(simpleQuestion);
            csUserQuestionVo.setCsUserInfo(userMap.get(csUserQuestion.getUserId()));
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
                            value = "3000")
            }

    )
    private PageQuestionInfo getPageQuestionInfo(PageInfo pageInfo, ActivityStatus activityStatus, String userId) {
        Page<CsUserQuestion> questionPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        IPage<CsUserQuestion> pageIssues = null;
        if (activityStatus.code.equals(ActivityStatus.RESOLVED.code)) {
            pageIssues = csUserQuestionMapper.getPageResolvedIssues(questionPage);
        } else {
            pageIssues = csUserQuestionMapper.getPageUnresolvedIssues(questionPage);
        }
        List<CsUserQuestion> questions = pageIssues.getRecords();
        log.info("Get {} question -- page [{}] -- [{}]", activityStatus.description, pageInfo.getPage(), questions);
        PageQuestionInfo pageQuestionInfo = new PageQuestionInfo();
        pageQuestionInfo.setQuestionInfos(getCsQuestionVo(questions, userId));
        pageQuestionInfo.setHasMore(pageIssues.getPages() > pageInfo.getPage());
        return pageQuestionInfo;
    }

    private PageQuestionInfo getPageQuestionInfoFallBack(PageInfo pageInfo, ActivityStatus activityStatus, String userId
    ,Throwable throwable) {
        log.error("user [{}] get page [{}] [{}] question into fallback method : [{}]", userId, pageInfo.getPage(),
                activityStatus.description,throwable.getMessage());
        return null;
    }
}




