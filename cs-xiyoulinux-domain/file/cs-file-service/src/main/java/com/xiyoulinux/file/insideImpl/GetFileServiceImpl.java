package com.xiyoulinux.file.insideImpl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.file.mapper.CsActivityCommentFileMapper;
import com.xiyoulinux.file.mapper.CsActivityFileMapper;
import com.xiyoulinux.file.entity.CsActivityCommentFile;
import com.xiyoulinux.file.entity.CsActivityFile;
import com.xiyoulinux.file.service.GetFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.*;

/**
 * @author qkm
 */
@DubboService
@Slf4j
public class GetFileServiceImpl implements GetFileService {

    private final CsActivityFileMapper csActivityFileMapper;

    private final CsActivityCommentFileMapper csActivityCommentFileMapper;

    public GetFileServiceImpl(CsActivityFileMapper csActivityFileMapper, CsActivityCommentFileMapper csActivityCommentFileMapper) {
        this.csActivityFileMapper = csActivityFileMapper;
        this.csActivityCommentFileMapper = csActivityCommentFileMapper;
    }

    @HystrixCommand(
            groupKey = "file",
            // 舱壁模式
            threadPoolKey = "file",
            // 后备模式
            fallbackMethod = "getFileUrlByActivityIdFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000")
            },
            // 舱壁模式
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "80")
            }

    )
    @Override
    public HashMap<String, List<String>> getFileUrlByActivityId(List<String> activityIdList) {
        List<CsActivityFile> activityFiles = csActivityFileMapper.selectAllByActivityIdList(activityIdList);
        if (null == activityFiles || activityFiles.size() == 0) {
            log.info("Get files --- about the activity [{}] but has not files", activityIdList);
            return new HashMap<>(1);
        }
        HashMap<String, List<String>> activityIdFilesMap = new HashMap<>(16);
        for (CsActivityFile activityFile : activityFiles) {
            activityIdFilesMap.put(activityFile.getActivityId(),
                    Arrays.asList(activityFile.getActivityFileUrl().split(",")));
        }
        log.info("Get files --- all files [{}] of activity [{}]", activityIdFilesMap,
                activityIdList);
        return activityIdFilesMap;
    }

    public HashMap<String, List<String>> getFileUrlByActivityIdFallBack(List<String> activityIdList) {
        log.error("get activity file into fallback method");
        HashMap<String, List<String>> activityIdFilesMap = new HashMap<>(16);
        for (String activityId : activityIdList) {
            activityIdFilesMap.put(activityId, Collections.singletonList("正在向你赶来..."));
        }
        return activityIdFilesMap;
    }

    @HystrixCommand(
            groupKey = "file",
            // 舱壁模式
            threadPoolKey = "file",
            // 后备模式
            fallbackMethod = "getFileUrlByCommentIdFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000")
            }
    )
    @Override
    public HashMap<String, List<String>> getFileUrlByCommentId(List<String> commentIdList) {
        List<CsActivityCommentFile> commentFiles = csActivityCommentFileMapper.selectAllByCommentIdList(commentIdList);
        if (null == commentIdList || commentIdList.size() == 0) {
            log.info("Get files --- about the comment [{}] but has not files", commentIdList);
            return new HashMap<>(1);
        }
        HashMap<String, List<String>> commentIdFilesMap = new HashMap<>(16);
        for (CsActivityCommentFile commentFile : commentFiles) {
            commentIdFilesMap.put(commentFile.getCommentId(),
                    Arrays.asList(commentFile.getCommentFileUrl().split(",")));
        }
        log.info("Get files --- all files [{}] of comment [{}]", commentIdFilesMap, commentIdList);
        return commentIdFilesMap;
    }

    public HashMap<String, List<String>> getFileUrlByCommentIdFallBack(List<String> commentIdList) {
        log.error("get comment file into fallback method");
        HashMap<String, List<String>> commentIdFilesMap = new HashMap<>(16);
        for (String commentId : commentIdList) {
            commentIdFilesMap.put(commentId, Collections.singletonList("正在向你赶来..."));
        }
        return commentIdFilesMap;
    }
}
