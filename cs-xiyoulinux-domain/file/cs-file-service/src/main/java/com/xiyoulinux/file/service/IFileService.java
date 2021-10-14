package com.xiyoulinux.file.service;

import com.xiyoulinux.file.pojo.CsActivityCommentFile;
import com.xiyoulinux.file.pojo.CsActivityFile;
import com.xiyoulinux.file.pojo.CsUserFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @author qkm
 */
public interface IFileService {
    /**
     * 上传文件到云服务器
     *
     * @param files 文件列表
     * @return 图片的url
     */
    String uploadFile(MultipartFile[] files);

    /**
     * 保存用户图片信息到数据库
     *
     * @param user 用户信息
     */
    void saveUserFiles(CsUserFile user);

    /**
     * 保存评论文件到数据库
     *
     * @param comment 文件对象
     */
    void saveCommentFiles(CsActivityCommentFile comment);

    /**
     * 保存动态文件到数据库
     *
     * @param activity 文件对象
     */
    void saveActivityFiles(CsActivityFile activity);

    /**
     * 根据动态id查找文件
     *
     * @param activityId 动态id集合
     * @return 所有的文件
     */
    HashMap<String, List<String>> getFileUrlByActivityId(List<String> activityId);

    /**
     * 根据评论id查找文件
     *
     * @param commentId 评论id集合
     * @return 所有的文件
     */
    HashMap<String, List<String>> getFileUrlByCommentId(List<String> commentId);

    /**
     * 删除评论对应的文件
     *
     * @param commentId 评论id
     */
    void deleteCommentFile(String commentId);

    /**
     * 删除动态对应的文件以及该评论对应的文件信息
     *
     * @param activityId 动态id
     */
    void deleteActivityAndCommentFile(String activityId);

}
