package com.xiyoulinux.activity.comment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.comment.bo.ActivityCommentNumber;
import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.bo.CsUserLikesBo;
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
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.file.service.IUploadFileService;
import com.xiyoulinux.utils.RedisOperator;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.n3r.idworker.Sid;
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

    @DubboReference
    private IUploadFileService iUploadFileService;


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
    public PageCommentInfo getPageCommentsByActivityIdAndUserId(PageInfo pageInfo,
                                                                String activityId,
                                                                String userId) {
        IPage<CsUserActivityComment> csUserActivityCommentPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        IPage<CsUserActivityComment> csUserActivityCommentIPage =
                csUserCommentMapper.selectPageByActivityId(activityId, csUserActivityCommentPage);
        return getPageCommentInfo(pageInfo, activityId, userId, csUserActivityCommentIPage);
    }


    public PageCommentInfo getPageCommentsByActivityIdAndUserIdFallBack(PageInfo pageInfo,
                                                                        String activityId,
                                                                        String userId) {
        log.error("user [{}] get activity [{}] page [{}] comments into fallback",
                userId, activityId, pageInfo.getPage());
        return null;
    }

     @HystrixCommand(
            groupKey = "comment",
            // 舱壁模式
            threadPoolKey = "comment",
            // 后备模式
            fallbackMethod = "getPageCommentsByActivityIdAndUserIdOrderByLikesFallback",
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
    public PageCommentInfo getPageCommentsByActivityIdAndUserIdOrderByLikes(PageInfo pageInfo,
                                                                String activityId,
                                                                String userId) {
        IPage<CsUserActivityComment> csUserActivityCommentPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        IPage<CsUserActivityComment> csUserActivityCommentIPage =
                csUserCommentMapper.selectPageByActivityIdOrderByLikes(activityId, csUserActivityCommentPage);
        return getPageCommentInfo(pageInfo, activityId, userId, csUserActivityCommentIPage);
    }


    public PageCommentInfo getPageCommentsByActivityIdAndUserIdOrderByLikesFallback(PageInfo pageInfo,
                                                                String activityId,
                                                                String userId) {
        log.error("user [{}] get activity [{}]  page [{}] comments orderBy likes into fallback",
                userId, activityId, pageInfo.getPage());
        return null;
    }


    private PageCommentInfo getPageCommentInfo(PageInfo pageInfo, String activityId, String userId, IPage<CsUserActivityComment> csUserActivityCommentIPage) {
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
        pageCommentInfo.setHasMore(csUserActivityCommentIPage.getPages() > pageInfo.getPage());


        //当前页面的评论信息
        pageCommentInfo.setCommentInfo(getCsActivityCommentVo(commentList, userId));

        return pageCommentInfo;
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
    public String likesComment(CsUserLikesBo csUserLikesBo) {
        CsUserLikes csUserLikes = new CsUserLikes();
        BeanUtils.copyProperties(csUserLikesBo, csUserLikes);
        csUserLikes.setId(sid.nextShort());
        try {
            csUserLikesMapper.insert(csUserLikes);
            log.info("userId [{}] likes commentId [{}]", csUserLikes.getCsUserId(), csUserLikes.getCsCommentId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        log.info("commentId [{}] add likes number [{}]", csUserLikes.getCsCommentId(),
                redisOperator.incr("LIKE:" + csUserLikes.getCsCommentId(), 1));
        return "点赞成功";
    }

    public String likesCommentFallBack(CsUserLikesBo csUserLikesBo) {
        log.error("user [{}] like comment [{}] into fallback", csUserLikesBo.getCsUserId()
                , csUserLikesBo.getCsCommentId());
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

        List<String> fileUrl = null;
        if (files != null) {
            //上传评论内容中的文件信息到文件服务
            fileUrl = iUploadFileService.uploadOSS(files);
        }

        csUserActivityComment.setCommentFiles(fileUrl);

        //插入评论
        csUserCommentMapper.insert(csUserActivityComment);
        log.info("Insert comment success -- add one comment [{}] to database", comment);


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
        log.info("Delete comment success --- all comments of activity [{}]", activityId);
    }

    @Override
    public void deleteLikesByCsActivityId(String activityId) {
        csUserLikesMapper.deleteLikesByCsActivityId(activityId);
    }

    /**
     * 根据id从微服务获取用户、文件信息
     *
     * @return 评论信息集合
     */
    public List<CsUserActivityCommentVo> getCsActivityCommentVo(List<CsUserActivityComment> commentList, String userId) {
        //用户id
        Set<String> idList = commentList.stream().map(CsUserActivityComment::getUserId).collect(Collectors.toSet());
        log.info("get all userId [{}] of comments [{}]", idList, commentList);

        //调用用户服务获取用户信息
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);
        log.info("call user server get userInfo success --- get all userInfo [{}] of comment [{}]", userMap, commentList);

        //构造评论vo
        List<CsUserActivityCommentVo> csActivityVos = new ArrayList<>();
        commentList.forEach(comment -> {
            CsUserActivityCommentVo commentVo = new CsUserActivityCommentVo();
            CsUserActivityCommentVo.ActivityComment simpleComment = CsUserActivityCommentVo.ActivityComment.to(comment);
            commentVo.setActivityComment(simpleComment);
            commentVo.setCsUserInfo(userMap.get(comment.getUserId()));
            commentVo.setLike(Objects.equals(userId, comment.getUserId()));
            csActivityVos.add(commentVo);
        });
        log.info("Fill comment object over --- comment objects that populate attributes [{}]", commentList);
        return csActivityVos;
    }

}






