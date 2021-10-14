package com.xiyoulinux.file.insideImpl;


import com.xiyoulinux.file.pojo.CsActivityCommentFile;
import com.xiyoulinux.file.pojo.CsActivityFile;
import com.xiyoulinux.file.pojo.CsUserFile;
import com.xiyoulinux.file.service.IFileService;
import com.xiyoulinux.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import com.xiyoulinux.idworker.Sid;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author qkm
 */
@DubboService
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UploadFileServiceImpl implements UploadFileService {

    @Resource
    private IFileService IFileService;

    @Resource
    private Sid sid;

    /**
     * TODO
     *
     * @param files  文件列表
     * @param userId 用户id
     */
    @Override
    public void uploadUserFile(MultipartFile[] files, String userId) {
        String filesUrls = upload(files);
        CsUserFile csUserFile = new CsUserFile(sid.nextShort(), userId, filesUrls);
        IFileService.saveUserFiles(csUserFile);
    }

    /**
     * @param files     文件列表
     * @param commentId 评论id
     */
    @Override
    public void uploadCommentFile(MultipartFile[] files, String commentId, String activityId) {
        String fileUrl = upload(files);
        CsActivityCommentFile commentFile = new CsActivityCommentFile(
                sid.nextShort(), commentId, fileUrl, activityId);
        IFileService.saveCommentFiles(commentFile);
    }

    @Override
    public void uploadActivityFile(MultipartFile[] files, String activityId) {
        String fileUrl = upload(files);
        CsActivityFile activityFile = new CsActivityFile(
                sid.nextShort(), activityId, fileUrl);
        IFileService.saveActivityFiles(activityFile);
    }

    /**
     * 上传至阿里云oss
     *
     * @param files 文件集合
     */
    public String upload(MultipartFile[] files) {
        //阿里云oss
        return IFileService.uploadFile(files);
    }
}
