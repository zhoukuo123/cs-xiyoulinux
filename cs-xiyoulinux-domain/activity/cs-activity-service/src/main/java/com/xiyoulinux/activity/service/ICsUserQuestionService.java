package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.bo.CsUserQuestionUpdateBo;
import com.xiyoulinux.activity.vo.PageQuestionInfo;
import com.xiyoulinux.activity.vo.QuestionNumber;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ActivityStatus;


/**
 * @author qkm
 */
public interface ICsUserQuestionService {
    /**
     * 获取未解决的问题
     *
     * @param pageInfo 分页
     * @return 未解决问题集合
     */
    PageQuestionInfo getPageUnresolvedIssues(PageInfo pageInfo, String userId);

    /**
     * 获取已解决的问题
     *
     * @param pageInfo f分页
     * @return 已解决问题集合
     */
    PageQuestionInfo getPageResolvedIssues(PageInfo pageInfo, String userId);

    /**
     * 更新问题状态
     *
     * @param csUserQuestionUpdateBo 要更新的问题对象
     */
    void updateQuestionStatus(CsUserQuestionUpdateBo csUserQuestionUpdateBo);

    /**
     * 获取问题的总数
     *
     * @return 问题总数对象 {@link QuestionNumber}
     */
    QuestionNumber getQuestionNumber();

}
