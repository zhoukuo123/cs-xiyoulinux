package com.xiyoulinux.file.insideImpl;

import com.xiyoulinux.file.mapper.CsActivityCommentFileMapper;
import com.xiyoulinux.file.mapper.CsActivityFileMapper;
import com.xiyoulinux.file.service.DeleteFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;


/**
 * @author qkm
 */
@Service
@Slf4j
public class DeleteFileServiceImpl implements DeleteFileService {

    private final CsActivityFileMapper csActivityFileMapper;

    private final CsActivityCommentFileMapper csActivityCommentFileMapper;

    public DeleteFileServiceImpl(CsActivityFileMapper csActivityFileMapper,
                                 CsActivityCommentFileMapper csActivityCommentFileMapper) {
        this.csActivityFileMapper = csActivityFileMapper;
        this.csActivityCommentFileMapper = csActivityCommentFileMapper;
    }

    @Override
    public void deleteCommentFile(String commentId) {
        csActivityCommentFileMapper.delByCommentId(commentId);
        log.info("Delete files --- about the comment [{}]", commentId);
    }

    @Override
    public void deleteActivityAndCommentFile(String activityId) {
        csActivityFileMapper.delActivityFileByActivityId(activityId);
        log.info("Delete files --- about the activityId [{}]", activityId);
        csActivityCommentFileMapper.delCommentFileByActivityId(activityId);
        log.info("Delete files --- about the comment down the activityId [{}]", activityId);
    }
}
