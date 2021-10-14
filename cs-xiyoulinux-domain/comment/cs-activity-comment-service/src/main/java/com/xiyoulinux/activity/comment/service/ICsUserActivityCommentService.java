package com.xiyoulinux.activity.comment.service;

import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.bo.CsUserInfoAndId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author qkm
 */
public interface ICsUserActivityCommentService {
    /**
     * 根据动态id获取动态的所有评论
     *
     * @param activityId 动态id
     * @param page       分页
     * @return 评论集合
     */
    PageCommentInfo getPageCommentsByActivityId(String activityId, int page);

    /**
     * 给评论点赞
     *
     * @param id 评论id
     */
    void likesComment(String id);

    /**
     * 给评论取消点赞
     *
     * @param id 评论id
     */
    void dislikeComment(String id);

    /**
     * 增加评论到指定的动态
     *
     * @param comment 评论对象
     * @param files   文件
     * @return 用户信息和动态id
     */
    CsUserInfoAndId addComment(CsUserActivityCommentBo comment, MultipartFile[] files);

    /**
     * 获取当前动态的评论数
     *
     * @param activityIdList 动态id
     * @return 评论数目
     */
    Map<String, Long> getCommentNumber(List<String> activityIdList);

    /**
     * 删除当前动态的评论
     *
     * @param activityId 动态id
     */
    void deleteComments(String activityId);
}
