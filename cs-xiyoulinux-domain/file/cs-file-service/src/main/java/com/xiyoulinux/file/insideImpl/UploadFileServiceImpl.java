package com.xiyoulinux.file.insideImpl;


import com.xiyoulinux.file.mapper.CsActivityCommentFileMapper;
import com.xiyoulinux.file.mapper.CsActivityFileMapper;
import com.xiyoulinux.file.mapper.CsUserFileMapper;
import com.xiyoulinux.file.entity.CsActivityCommentFile;
import com.xiyoulinux.file.entity.CsActivityFile;
import com.xiyoulinux.file.entity.CsUserFile;
import com.xiyoulinux.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.n3r.idworker.Sid;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author qkm
 */
@DubboService
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    private final CsActivityFileMapper csActivityFileMapper;

    private final CsActivityCommentFileMapper csActivityCommentFileMapper;

    private final CsUserFileMapper csUserFileMapper;

    private final Sid sid;

    public UploadFileServiceImpl(CsActivityFileMapper csActivityFileMapper,
                                 CsActivityCommentFileMapper csActivityCommentFileMapper,
                                 CsUserFileMapper csUserFileMapper,
                                 Sid sid) {
        this.csActivityFileMapper = csActivityFileMapper;
        this.csActivityCommentFileMapper = csActivityCommentFileMapper;
        this.csUserFileMapper = csUserFileMapper;
        this.sid = sid;
    }

    /**
     * TODO
     *
     * @param files  文件列表
     * @param userId 用户id
     */
    @Override
    public List<String> uploadUserFile(MultipartFile[] files, String userId) {
        String fileUrl = upload(files);
        CsUserFile csUserFile = new CsUserFile(sid.nextShort(), userId, fileUrl);
        try {
            csUserFileMapper.insert(csUserFile);
        } catch (Exception e) {
            //删除oss的文件
            //抛出异常
            log.error("insert user file error:[{}]", csUserFile);
            throw new RuntimeException("insert user file error");
        }
        log.info("Insert files --- about user [{}]", csUserFile);
        return Arrays.asList(fileUrl.split(","));
    }

    /**
     * @param files     文件列表
     * @param commentId 评论id
     */
    @Override
    public List<String> uploadCommentFile(MultipartFile[] files, String commentId, String activityId) {
        String fileUrl = upload(files);
        CsActivityCommentFile commentFile = new CsActivityCommentFile(
                sid.nextShort(), commentId, fileUrl, activityId);
        try {
            csActivityCommentFileMapper.insert(commentFile);
        } catch (Exception e) {
            //删除oss的文件
            //抛出异常
            log.error("insert comment file [{}] error -- delete comment file from oss", commentFile);
            throw new RuntimeException("insert comment file error");
        }
        log.info("Insert files ---- about the comment [{}]", commentFile);
        return Arrays.asList(fileUrl.split(","));
    }

    @Override
    public List<String> uploadActivityFile(MultipartFile[] files, String activityId) {
        String fileUrl = upload(files);
        CsActivityFile activityFile = new CsActivityFile(
                sid.nextShort(), activityId, fileUrl);
        try {
            csActivityFileMapper.insert(activityFile);
        } catch (Exception e) {
            //删除oss的文件
            log.info("insert activity file [{}] error -- delete upload activity file from oss", activityFile);
            //抛出异常
            log.error("insert activity file error:[{}]", activityFile);
            throw new RuntimeException("insert activity file error");
        }
        log.info("Insert activity files [{}] --- about the activity [{}]", activityFile, activityId);
        return Arrays.asList(fileUrl.split(","));
    }

    /**
     * 上传至阿里云oss
     *
     * @param files 文件集合
     */
    public String upload(MultipartFile[] files) {
        //阿里云oss
        return "";
    }
}
