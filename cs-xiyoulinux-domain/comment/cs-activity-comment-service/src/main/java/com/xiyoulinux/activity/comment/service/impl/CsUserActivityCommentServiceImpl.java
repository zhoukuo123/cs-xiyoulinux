package com.xiyoulinux.activity.comment.service.impl;

import com.alibaba.fastjson.JSON;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            // ????????????
            threadPoolKey = "comment",
            // ????????????
            fallbackMethod = "getPageCommentsByActivityIdAndUserIdFallBack",
            // ???????????????
            commandProperties = {
                    // ????????????, ????????????, ????????? fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            },
            // ????????????
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

        return getPageCommentInfo(pageInfo, activityId, userId, csUserActivityCommentIPage, false);
    }


    public PageCommentInfo getPageCommentsByActivityIdAndUserIdFallBack(PageInfo pageInfo,
                                                                        String activityId,
                                                                        String userId,
                                                                        Throwable throwable) {
        log.error("user [{}] get activity [{}] page [{}] size [{}] comments into fallback : [{}]",
                userId, activityId, pageInfo.getPage(), pageInfo.getSize(), throwable.getMessage());
        return null;
    }

    @HystrixCommand(
            groupKey = "comment",
            // ????????????
            threadPoolKey = "comment",
            // ????????????
            fallbackMethod = "getPageCommentsByActivityIdAndUserIdOrderByLikesFallback",
            // ???????????????
            commandProperties = {
                    // ????????????, ????????????, ????????? fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            },
            // ????????????
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
        return getPageCommentInfo(pageInfo, activityId, userId, csUserActivityCommentIPage, true);
    }


    public PageCommentInfo getPageCommentsByActivityIdAndUserIdOrderByLikesFallback(PageInfo pageInfo,
                                                                                    String activityId,
                                                                                    String userId,
                                                                                    Throwable throwable) {
        log.error("user [{}] get activity [{}] page [{}] size [{}] comments orderBy likes into fallback : [{}]",
                userId, activityId, pageInfo.getPage(), pageInfo.getSize(), throwable.getStackTrace());
        return null;
    }


    private PageCommentInfo getPageCommentInfo(PageInfo pageInfo, String activityId, String userId,
                                               IPage<CsUserActivityComment> csUserActivityCommentIPage,
                                               boolean isSort) {
        List<CsUserActivityComment> commentList = csUserActivityCommentIPage.getRecords();

        PageCommentInfo pageCommentInfo = new PageCommentInfo();

        if (!commentList.isEmpty()) {
            //?????????????????????2????????????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????
            commentList.forEach(comment -> {
                String commentLikesNumber = redisOperator.get("LIKE:" + comment.getId());
                if (commentLikesNumber != null) {
                    comment.setCommentLikes(Integer.parseInt(commentLikesNumber)
                            + comment.getCommentLikes());
                }
            });
        }

        //?????????????????????
        pageCommentInfo.setHasMore(csUserActivityCommentIPage.getPages() > pageInfo.getPage());

        List<CsUserActivityCommentVo> csActivityCommentVo = getCsActivityCommentVo(commentList, userId);

        if (isSort) {
            csActivityCommentVo.sort(new Comparator<CsUserActivityCommentVo>() {
                @Override
                public int compare(CsUserActivityCommentVo o1, CsUserActivityCommentVo o2) {
                    return o2.getActivityComment().getCommentLikes() - o1.getActivityComment().getCommentLikes();
                }
            });
        }
        //???????????????????????????
        pageCommentInfo.setCommentInfo(csActivityCommentVo);


        log.info("get activityId [{}] page [{}]  size [{}] comment", activityId, pageInfo.getPage(),
                pageInfo.getSize());
        return pageCommentInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String likesComment(CsUserLikesBo csUserLikesBo) {
        CsUserLikes csUserLikes = new CsUserLikes();
        BeanUtils.copyProperties(csUserLikesBo, csUserLikes);
        csUserLikes.setId(sid.nextShort());
        try {
            csUserLikesMapper.insert(csUserLikes);
        } catch (Exception e) {
            log.error("insert user [{}] likes commentId [{}] error [{}]", csUserLikes.getCsUserId(),
                    csUserLikes.getCsCommentId(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            redisOperator.incr("LIKE:" + csUserLikes.getCsCommentId(), 1);
        } catch (Exception e) {
            log.error("likes commentId [{}] redis error [{}]", csUserLikes.getCsCommentId(), e.getMessage());
            throw new RuntimeException("likes comment redis error");
        }
        log.info("commentId [{}] add likes", csUserLikes.getCsCommentId());
        return "????????????";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String dislikeComment(String commentId, String userId) {
        try {
            csUserLikesMapper.deleteByCsUserIdAndCsCommentId(userId, commentId);
        } catch (Exception e) {
            log.error("delete user [{}] likes commentId [{}] error [{}]", userId, commentId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            redisOperator.decr("LIKE:" + commentId, 1);
        } catch (Exception e) {
            log.error("dislikes commentId [{}] redis error [{}]", commentId, e.getMessage());
            throw new RuntimeException("dislikes comment redis error");
        }
        log.info("commentId [{}] decr likes ", commentId);
        return "????????????";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CsUserInfoAndIdAndFileInfo addComment(CsUserActivityCommentBo comment, MultipartFile[] files) {
        CsUserActivityComment csUserActivityComment = new CsUserActivityComment();
        BeanUtils.copyProperties(comment, csUserActivityComment);
        csUserActivityComment.setId(sid.nextShort());

        List<String> fileUrl = null;
        if (files != null && files.length != 0) {
            //???????????????????????????????????????????????????
            List<byte[]> bytes = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    bytes.add(file.getBytes());
                } catch (IOException e) {
                    log.error("upload pic convert byte error");
                    throw new RuntimeException("upload pic convert byte error");
                }
            }
            fileUrl = iUploadFileService.uploadOSS(bytes);
        }

        csUserActivityComment.setCommentFiles(JSON.toJSONString(fileUrl));

        //????????????
        csUserCommentMapper.insert(csUserActivityComment);
        log.info("Insert comment success -- add one comment [{}] to database", comment);


        //????????????????????????
        CsUserInfo csUserInfo = interService.interCallPeople(csUserActivityComment.getUserId());
        CsUserInfoAndIdAndFileInfo userAndActivityId = new CsUserInfoAndIdAndFileInfo();
        userAndActivityId.setCsUserInfo(csUserInfo);
        userAndActivityId.setId(csUserActivityComment.getId());
        userAndActivityId.setFiles(JSON.toJSONString(fileUrl));
        return userAndActivityId;
    }

    @HystrixCommand(
            groupKey = "comment",
            // ????????????
            threadPoolKey = "comment",
            // ????????????
            fallbackMethod = "getCommentNumberFallback",
            // ???????????????
            commandProperties = {
                    // ????????????, ????????????, ????????? fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000")
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
        return commentMap;
    }

    public Map<String, Long> getCommentNumberFallback(List<String> activityIdList, Throwable throwable) {
        log.error("get comment number into fallback method : [{}]", throwable.getMessage());
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

    @Override
    public void mergeLikes(Map<String, Integer> likes) {
        csUserCommentMapper.mergeLikes(likes);
    }

    /**
     * ??????id???????????????????????????????????????
     *
     * @return ??????????????????
     */
    public List<CsUserActivityCommentVo> getCsActivityCommentVo(List<CsUserActivityComment> commentList, String userId) {
        if (null == commentList || commentList.size() == 0) {
            return null;
        }
        //??????id
        Set<String> userIdList = commentList.stream().map(CsUserActivityComment::getUserId).collect(Collectors.toSet());

        //????????????????????????????????????
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(userIdList);

        Set<String> commentIdList = commentList.stream().map(CsUserActivityComment::getId).collect(Collectors.toSet());

        List<String> likeCommentId = csUserLikesMapper.selectLikesUserId(commentIdList, userId);
        //????????????vo
        List<CsUserActivityCommentVo> csActivityVos = new ArrayList<>();
        commentList.forEach(comment -> {
            CsUserActivityCommentVo commentVo = new CsUserActivityCommentVo();
            CsUserActivityCommentVo.ActivityComment simpleComment = CsUserActivityCommentVo.ActivityComment.to(comment);
            commentVo.setActivityComment(simpleComment);
            commentVo.setCsUserInfo(userMap.get(comment.getUserId()));
            commentVo.setLike(likeCommentId.contains(comment.getId()));
            csActivityVos.add(commentVo);
        });
        return csActivityVos;
    }

}






