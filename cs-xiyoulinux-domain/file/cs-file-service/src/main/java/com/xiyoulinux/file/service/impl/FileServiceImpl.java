package com.xiyoulinux.file.service.impl;

import com.xiyoulinux.file.mapper.CsActivityCommentFileMapper;
import com.xiyoulinux.file.mapper.CsActivityFileMapper;
import com.xiyoulinux.file.mapper.CsUserFileMapper;
import com.xiyoulinux.file.pojo.CsActivityCommentFile;
import com.xiyoulinux.file.pojo.CsActivityFile;
import com.xiyoulinux.file.pojo.CsUserFile;
import com.xiyoulinux.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author qkm
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class FileServiceImpl implements IFileService {

    @Resource
    private CsActivityFileMapper csActivityFileMapper;

    @Resource
    private CsActivityCommentFileMapper csActivityCommentFileMapper;

    @Resource
    private CsUserFileMapper csUserFileMapper;


    @Override
    public String uploadFile(MultipartFile[] files) {
        log.info("Upload files to oss");
        //return 的字符串以逗号分割
        return null;
    }

    @Override
    public void saveUserFiles(CsUserFile user) {
        csUserFileMapper.insert(user);
        log.info("Insert files --- about user [{}]", user);
    }

    @Override
    public void saveCommentFiles(CsActivityCommentFile comment) {
        csActivityCommentFileMapper.insert(comment);
        log.info("Insert files ---- about the comment [{}]", comment);
    }

    @Override
    public void saveActivityFiles(CsActivityFile activity) {
        csActivityFileMapper.insert(activity);
        log.info("Insert files --- about the activity [{}]", activity);
    }


    @Override
    public HashMap<String, List<String>> getFileUrlByActivityId(List<String> activityIdList) {
        List<CsActivityFile> activityFiles = csActivityFileMapper.selectAllByActivityIdList(activityIdList);
        if (null == activityFiles || activityFiles.size() == 0) {
            log.info("Get files --- about the activity [{}] but has not files", activityIdList);
            return null;
        }
        HashMap<String, List<String>> activityIdFilesMap = new HashMap<>(16);
        for (CsActivityFile activityFile : activityFiles) {
            String[] fileUrls = activityFile.getActivityFileUrl().split(",");
            List<String> fileUrlsList = new ArrayList<>(fileUrls.length);
            Collections.addAll(fileUrlsList, fileUrls);
            activityIdFilesMap.put(activityFile.getActivityId(), fileUrlsList);
        }
        log.info("Get files --- all files [{}] of activity [{}]", activityIdFilesMap,
                activityIdList);
        return activityIdFilesMap;
    }

    @Override
    public HashMap<String, List<String>> getFileUrlByCommentId(List<String> commentIdList) {
        List<CsActivityCommentFile> commentFiles = csActivityCommentFileMapper.selectAllByCommentIdList(commentIdList);
        if (null == commentIdList || commentIdList.size() == 0) {
            log.info("Get files --- about the comment [{}] but has not files", commentIdList);
            return null;
        }
        HashMap<String, List<String>> commentIdFilesMap = new HashMap<>(16);
        for (CsActivityCommentFile commentFile : commentFiles) {
            String[] fileUrls = commentFile.getCommentFileUrl().split(",");
            List<String> fileUrlsList = new ArrayList<>(fileUrls.length);
            Collections.addAll(fileUrlsList, fileUrls);
            commentIdFilesMap.put(commentFile.getCommentId(), fileUrlsList);
        }
        log.info("Get files --- all files [{}] of comment [{}]", commentIdFilesMap, commentIdList);
        return commentIdFilesMap;
    }

    @Override
    public void deleteCommentFile(String commentId) {
        csActivityCommentFileMapper.delByCommentId(commentId);
        log.info("Delete files --- about the comment [{}]", commentId);
    }

    @Override
    public void deleteActivityAndCommentFile(String activityId) {
        csActivityFileMapper.delActivityByActivityId(activityId);
        log.info("Delete files --- about the activityId [{}]", activityId);
        csActivityCommentFileMapper.delCommentByActivityId(activityId);
        log.info("Delete files --- about the comment down the activityId [{}]", activityId);
    }

}
