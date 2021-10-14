package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.vo.PageQuestionInfo;
import com.xiyoulinux.enums.ActivityStatus;


/**
 * @author qkm
 */
public interface ICsUserQuestionService {
    /**
     * 获取未解决的问题
     *
     * @param page 分页
     * @return 未解决问题集合
     */
    PageQuestionInfo getPageUnresolvedIssues(int page);

    /**
     * 获取已解决的问题
     *
     * @param page f分页
     * @return 已解决问题集合
     */
    PageQuestionInfo getPageResolvedIssues(int page);

    /**
     * 管理员更新问题状态
     *
     * @param id             问题id
     * @param questionStatus 更新任务的状态
     */
    void updateQuestionStatus(String id, ActivityStatus questionStatus);

}
