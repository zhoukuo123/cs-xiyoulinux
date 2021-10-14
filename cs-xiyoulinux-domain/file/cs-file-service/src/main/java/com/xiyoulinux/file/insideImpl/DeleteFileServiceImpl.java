package com.xiyoulinux.file.insideImpl;

import com.xiyoulinux.file.service.DeleteFileService;
import com.xiyoulinux.file.service.IFileService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


/**
 * @author qkm
 */
@DubboService
public class DeleteFileServiceImpl implements DeleteFileService {

    @Resource
    private IFileService IFileService;

    @Override
    public void deleteCommentFile(String commentId) {
       IFileService.deleteCommentFile(commentId);
    }

    @Override
    public void deleteActivityAndCommentFile(String activityId) {
        IFileService.deleteActivityAndCommentFile(activityId);
    }
}
