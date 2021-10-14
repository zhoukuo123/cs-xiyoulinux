package com.xiyoulinux.file.service;

/**
 * @author qkm
 */
public interface DeleteFileService {

    /**
     * 删除评论对应的文件信息
     *
     * @param commentId 评论id
     */
    void deleteCommentFile(String commentId);

    /**
     * 删除动态对应的文件信息以及评论的文件信息
     *
     * @param activityId 动态id
     */
    void deleteActivityAndCommentFile(String activityId);
}
