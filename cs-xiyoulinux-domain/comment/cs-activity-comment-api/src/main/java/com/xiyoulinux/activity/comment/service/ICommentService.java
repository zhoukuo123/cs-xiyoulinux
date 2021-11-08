package com.xiyoulinux.activity.comment.service;

import java.util.List;
import java.util.Map;

/**
 * @author qkm
 */
public interface ICommentService {
    /**
     * 获取当前动态的评论数
     *
     * @param activityIdList 动态id
     * @return 评论数目
     */
    Map<String, Long> getCommentNumber(List<String> activityIdList);

    /**
     * 删除当前动态的所有评论信息
     *
     * @param activityId 动态id
     */
    void deleteComments(String activityId);
}
