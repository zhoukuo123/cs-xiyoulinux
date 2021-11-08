package com.xiyoulinux.activity.comment.service;

import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.bo.CsUserLikesBo;
import com.xiyoulinux.activity.comment.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.common.PageInfo;
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
     * @param pageInfo   分页对象
     * @param userId     用户id
     * @return 评论集合
     */
    PageCommentInfo getPageCommentsByActivityIdAndUserId(PageInfo pageInfo, String activityId, String userId);/**

     * 根据动态id获取动态的所有评论根据likes降序
     *
     * @param activityId 动态id
     * @param pageInfo   分页对象
     * @param userId     用户id
     * @return 评论集合
     */
    PageCommentInfo getPageCommentsByActivityIdAndUserIdOrderByLikes(PageInfo pageInfo, String activityId, String userId);

    /**
     * 给评论点赞
     *
     * @param csUserLikesBo {@link CsUserLikesBo}
     */
    String likesComment(CsUserLikesBo csUserLikesBo);

    /**
     * 给评论取消点赞
     *
     * @param commentId 评论id
     * @param userId    用户id
     */
    String dislikeComment(String commentId, String userId);

    /**
     * 增加评论到指定的动态
     *
     * @param comment 评论对象
     * @param files   文件
     * @return 用户信息和动态id
     */
    CsUserInfoAndIdAndFileInfo addComment(CsUserActivityCommentBo comment, MultipartFile[] files);

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


    /**
     * 删除点赞信息
     */
    void deleteLikesByCsActivityId(String activityId);


}
