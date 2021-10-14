package com.xiyoulinux.file.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author qkm
 */
public interface UploadFileService {
    /**
     * 上传云并保存至数据库
     *
     * @param files  文件列表
     * @param userId 用户id
     * TODO
     */
    void uploadUserFile(MultipartFile[] files, String userId);

    /**
     * 上传云并保存至数据库
     *
     * @param files  文件列表
     * @param commentId 评论id
     * @param activityId 动态id
     * TODO
     */
    void uploadCommentFile(MultipartFile[] files, String commentId,String activityId);

    /**
     * 上传云并保存至数据库
     *
     * @param files  文件列表
     * @param activityId 用户id
     * TODO
     */
    void uploadActivityFile(MultipartFile[] files, String activityId);
}
