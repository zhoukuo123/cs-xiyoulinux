package com.xiyoulinux.activity.comment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.comment.bo.ActivityCommentNumber;
import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.inter.InterService;
import com.xiyoulinux.activity.comment.mapper.CsUserLikesMapper;
import com.xiyoulinux.activity.comment.entity.CsUserLikes;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.mapper.CsUserCommentMapper;
import com.xiyoulinux.activity.comment.entity.CsUserActivityComment;
import com.xiyoulinux.activity.comment.vo.CsUserActivityCommentVo;
import com.xiyoulinux.activity.comment.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.file.service.GetFileService;
import com.xiyoulinux.file.service.UploadFileService;
import com.xiyoulinux.utils.RedisOperator;
import com.xiyoulinux.idworker.Sid;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
public class CsUserActivityCommentServiceImpl implements ICsUserActivityCommentService {


    private final CsUserCommentMapper csUserCommentMapper;

    private final CsUserLikesMapper csUserLikesMapper;

    private final RedisOperator redisOperator;

    private final Sid sid;

    private final InterService interService;

    @Reference
    private GetFileService getFileService;

    @Reference
    private UploadFileService uploadFileService;


    public CsUserActivityCommentServiceImpl(CsUserCommentMapper csUserCommentMapper,
                                            CsUserLikesMapper csUserLikesMapper,
                                            RedisOperator redisOperator,
                                            Sid sid,
                                            InterService interService) {
        this.csUserLikesMapper = csUserLikesMapper;
        this.redisOperator = redisOperator;
        this.csUserCommentMapper = csUserCommentMapper;
        this.sid = sid;
        this.interService = interService;
    }

    @HystrixCommand(
            groupKey = "comment",
            // 舱壁模式
            threadPoolKey = "comment",
            // 后备模式
            fallbackMethod = "getPageCommentsByActivityIdAndUserIdFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500")
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
    @SuppressWarnings("all")
    public PageCommentInfo getPageCommentsByActivityIdAndUserId(String activityId, int page, String userId) {
        IPage<CsUserActivityComment> csUserActivityCommentPage = new Page<>(page, 10);
        IPage<CsUserActivityComment> csUserActivityCommentIPage =
                csUserCommentMapper.selectPageByActivityId(activityId, csUserActivityCommentPage);
        List<CsUserActivityComment> commentList = csUserActivityCommentIPage.getRecords();
        log.info("Get comment --- all comments [{}] of activity [{}] has not userInfo and files",
                commentList, activityId);

        PageCommentInfo pageCommentInfo = new PageCommentInfo();

        //从缓存里面获取评论的点赞数目，只有点赞了该评论,才会被放进缓存，缓存只记录点赞的数目
        commentList.forEach(comment -> {
            String commentLikesNumber = redisOperator.get(comment.getId());
            if (commentLikesNumber != null) {
                comment.setCommentLikes(Integer.parseInt(commentLikesNumber) + comment.getCommentLikes());
            }
        });

        //是否还有下一页
        pageCommentInfo.setHasMore(csUserActivityCommentIPage.getPages() > page);


        //当前页面的评论信息
        pageCommentInfo.setCommentInfo(getCsActivityCommentVo(commentList, userId));

        return pageCommentInfo;
    }

    public PageCommentInfo getPageCommentsByActivityIdAndUserIdFallBack(String activityId, int page, String userId) {
        log.error("user [{}] get activity [{}] page [{}] comments into fallback", userId, activityId, page);
        return null;
    }

    @HystrixCommand(
            groupKey = "comment",
            // 舱壁模式
            threadPoolKey = "comment",
            // 后备模式
            fallbackMethod = "likesCommentFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500")
            }

    )
    @Override
    public String likesComment(String commentId, String userId) {
        String id = sid.nextShort();
        CsUserLikes csUserLikes = new CsUserLikes(id, commentId, userId);
        try {
            csUserLikesMapper.insert(csUserLikes);
            log.info("userId [{}] likes commentId [{}]", userId, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        log.info("commentId [{}] add likes number [{}]", commentId, redisOperator.incr("LIKE:" + commentId, 1));
        return "点赞成功";
    }

    public String likesCommentFallBack(String commentId, String userId) {
        log.error("user [{}] like comment [{}] into fallback", userId, commentId);
        return "努力发射中";
    }

    @HystrixCommand(
            groupKey = "comment",
            // 舱壁模式
            threadPoolKey = "comment",
            // 后备模式
            fallbackMethod = "dislikeCommentFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500")
            }
    )
    @Override
    public String dislikeComment(String commentId, String userId) {
        try {
            csUserLikesMapper.deleteByCsUserIdAndCsCommentId(userId, commentId);
            log.info("userId [{}] disLikes commentId [{}]", userId, commentId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        log.info("commentId [{}] add likes number [{}]", commentId, redisOperator.decr("DISLIKE:" + commentId, 1));
        return "取消成功";
    }

    public String dislikeCommentFallBack(String commentId, String userId) {
        log.error("user [{}] disLike comment [{}] into fallback", userId, commentId);
        return "努力发射中";
    }

    @Override
    @GlobalTransactional
    public CsUserInfoAndIdAndFileInfo addComment(CsUserActivityCommentBo comment, MultipartFile[] files) {
        CsUserActivityComment csUserActivityComment = new CsUserActivityComment();
        BeanUtils.copyProperties(comment, csUserActivityComment);
        csUserActivityComment.setId(sid.nextShort());
        log.info("Insert comment -- get CsUserActivityComment object [{}]", csUserActivityComment);

        //插入评论
        csUserCommentMapper.insert(csUserActivityComment);
        log.info("Insert comment -- add one comment [{}] to database", comment);

        List<String> fileUrl = null;
        if (files != null) {
            //上传评论内容中的文件信息到文件服务
            fileUrl = uploadFileService.uploadCommentFile(files, csUserActivityComment.getId(),
                    csUserActivityComment.getActivityId());
        }

        //用户服务增加降级
        CsUserInfo csUserInfo = interService.interCallPeople(csUserActivityComment.getUserId());
        CsUserInfoAndIdAndFileInfo userAndActivityId = new CsUserInfoAndIdAndFileInfo();
        userAndActivityId.setCsUserInfo(csUserInfo);
        userAndActivityId.setId(csUserActivityComment.getId());
        userAndActivityId.setFiles(fileUrl);
        return userAndActivityId;
    }

    @HystrixCommand(
            groupKey = "comment",
            // 舱壁模式
            threadPoolKey = "comment",
            // 后备模式
            fallbackMethod = "getCommentNumberFallback",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000")
            }

    )
    @Override
    public Map<String, Long> getCommentNumber(List<String> activityIdList) {
        List<ActivityCommentNumber> commentNumberByActivityId =
                csUserCommentMapper.getCommentNumberByActivityId(activityIdList);
        if (null == commentNumberByActivityId || commentNumberByActivityId.size() == 0) {
            log.info("Get comment number --- but activity [{}] has not comment", activityIdList);
            return new HashMap<>(1);
        }
        HashMap<String, Long> commentMap = new HashMap<>(16);
        for (ActivityCommentNumber activityCommentNumber : commentNumberByActivityId) {
            commentMap.put(activityCommentNumber.getActivityId(), activityCommentNumber.getCommentNumbers());
        }
        log.info("Get comments number --- comment number [{}] of activity [{}]", commentMap, activityIdList);
        return commentMap;
    }

    public Map<String, Long> getCommentNumberFallback(List<String> activityIdList) {
        log.error("get comment number into fallback method");
        HashMap<String, Long> commentMap = new HashMap<>(16);
        for (String activityId : activityIdList) {
            commentMap.put(activityId, -1L);
        }
        return commentMap;
    }

    @Override
    public void deleteComments(String activityId) {
        csUserCommentMapper.deleteByActivityId(activityId);
        log.info("Delete comment --- all comments of activity [{}]", activityId);
    }

    /**
     * 根据id从微服务获取用户、文件信息
     *
     * @return 评论信息集合
     */
    public List<CsUserActivityCommentVo> getCsActivityCommentVo(List<CsUserActivityComment> commentList, String userId) {
        //用户id
        Set<String> idList = commentList.stream().map(CsUserActivityComment::getUserId).collect(Collectors.toSet());
        log.info("Fill comment object --- get all userId [{}] of comments [{}]", idList, commentList);

        //评论id
        List<String> commentIdList = commentList.stream().map(CsUserActivityComment::getId).collect(Collectors.toList());
        log.info("Fill comment object --- get all commentId [{}] of comments [{}]", commentIdList, commentList);

        //调用用户服务获取用户信息
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);
        log.info("Fill comment object --- get all userInfo [{}] of comment [{}]", userMap, commentList);

        //调用文件服务获取评论的文件信息信息
        Map<String, List<String>> fileUrlByActivityIdMap = interService.interCallFile(commentIdList);
        log.info("Fill comment object --- get all files [{}] of comments [{}]", fileUrlByActivityIdMap, commentList);

        //构造评论vo
        List<CsUserActivityCommentVo> csActivityVos = new ArrayList<>();
        commentList.forEach(comment -> {
            CsUserActivityCommentVo commentVo = new CsUserActivityCommentVo();
            CsUserActivityCommentVo.ActivityComment simpleComment = CsUserActivityCommentVo.ActivityComment.to(comment);
            commentVo.setActivityComment(simpleComment);
            commentVo.setCsUserInfo(userMap.get(comment.getUserId()));
            commentVo.setLike(Objects.equals(userId, comment.getUserId()));
            commentVo.setActivityPicturesUrl(fileUrlByActivityIdMap.get(comment.getId()));
            csActivityVos.add(commentVo);
        });
        log.info("Fill comment object over --- comment objects that populate attributes [{}]", commentList);
        return csActivityVos;
    }

}





