package com.xiyoulinux.file.insideImpl;

import com.xiyoulinux.file.service.GetFileService;
import com.xiyoulinux.file.service.IFileService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author qkm
 */
@DubboService
public class GetFileServiceImpl implements GetFileService {
    @Resource
    private IFileService IFileService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public HashMap<String, List<String>> getFileUrlByActivityId(List<String> activityId) {
        return IFileService.getFileUrlByActivityId(activityId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public HashMap<String, List<String>> getFileUrlByCommentId(List<String> commentId) {
        return IFileService.getFileUrlByCommentId(commentId);
    }
}
