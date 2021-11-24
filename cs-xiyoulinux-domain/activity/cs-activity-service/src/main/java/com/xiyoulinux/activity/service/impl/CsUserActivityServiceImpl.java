package com.xiyoulinux.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.bo.CsUserActivityDeleteBo;
import com.xiyoulinux.activity.constant.ActivityConstant;
import com.xiyoulinux.activity.service.ActivitySource;
import com.xiyoulinux.activity.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.activity.inter.IntelService;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.mapper.CsUserTaskMapper;
import com.xiyoulinux.activity.entity.CsUserActivity;
import com.xiyoulinux.activity.entity.CsUserQuestion;
import com.xiyoulinux.activity.entity.CsUserTask;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.vo.CsUserActivityVo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.file.service.IUploadFileService;
import com.xiyoulinux.pojo.ActivityMessage;
import com.xiyoulinux.utils.DateUtil;
import com.xiyoulinux.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
@EnableBinding(ActivitySource.class)
public class CsUserActivityServiceImpl implements ICsUserActivityService {

    private static final String ME = "-1";

    private final CsUserActivityMapper csUserActivityMapper;

    private final CsUserQuestionMapper csUserQuestionMapper;

    private final CsUserTaskMapper csUserTaskMapper;

    private final RedisOperator redisOperator;

    private final IntelService intelService;

    private final Sid sid;

    private final ActivitySource activitySource;

    @DubboReference
    private IUploadFileService iUploadFileService;


    public CsUserActivityServiceImpl(CsUserActivityMapper csUserActivityMapper,
                                     CsUserQuestionMapper csUserQuestionMapper,
                                     CsUserTaskMapper csUserTaskMapper,
                                     ActivitySource activitySource,
                                     RedisOperator redisOperator,
                                     IntelService intelService,
                                     Sid sid) {
        this.csUserActivityMapper = csUserActivityMapper;
        this.csUserQuestionMapper = csUserQuestionMapper;
        this.activitySource = activitySource;
        this.csUserTaskMapper = csUserTaskMapper;
        this.redisOperator = redisOperator;
        this.intelService = intelService;
        this.sid = sid;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CsUserInfoAndIdAndFileInfo addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files) {
        CsUserActivity csUserActivity = new CsUserActivity();
        BeanUtils.copyProperties(csUserActivityBo, csUserActivity);
        //分布式id
        csUserActivity.setId(sid.nextShort());

        List<String> fileUrl = null;
        if (files != null && files.length != 0) {
            //上传评论内容中的文件信息到文件服务
            List<byte[]> bytes = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    bytes.add(file.getBytes());
                } catch (IOException e) {
                    log.error("upload pic convert byte error");
                    throw new RuntimeException("upload pic convert byte error");
                }
            }
            //调用图片服务将图片上传到云服务器
            fileUrl = iUploadFileService.uploadOSS(bytes);
        }
        csUserActivity.setActivityFiles(JSON.toJSONString(fileUrl));

        //插入总表
        csUserActivityMapper.insert(csUserActivity);
        log.info("Insert activity success --- [{}] to summary table", JSON.toJSONString(csUserActivity));

        //插入问题表
        if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.QUESTION.code)) {
            CsUserQuestion csUserQuestion = new CsUserQuestion(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityTitle(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityStatus(),
                    csUserActivity.getActivityFiles());

            csUserQuestionMapper.insert(csUserQuestion);
            log.info("Insert question success  --- [{}] to question table", JSON.toJSONString(csUserActivity));
            //插入任务表
        } else if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.TASK.code)) {
            CsUserTask csUserTask = new CsUserTask(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityTitle(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityEndTime(),
                    csUserActivity.getActivityStatus(),
                    csUserActivity.getActivityFiles());

            //插入数据库
            csUserTaskMapper.insert(csUserTask);
            log.info("Insert task success  --- [{}] to task table", JSON.toJSONString(csUserActivity));


            try {
                //删除缓存
                String deleteKey = judgeType(csUserActivity.getActivityStatus().code);
                //根据deleteKey先删除redis中存储的任务
                Set<String> keys = redisOperator.scan(deleteKey);
                if (keys != null && keys.size() != 0) {
                    redisOperator.delCollect(keys);
                    log.info("delete [{}] from redis success", deleteKey);
                }
            } catch (Exception e) {
                log.error("add task del redis error:[{}]", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }

        //用户服务增加降级---如果前面出现异常则回滾，否则执行到此处，如果调csUserActivityComment用用户服务有问题，则降级就是说用户服务获取信息使用降级后的，然后返回，
        //也不需要回滾
        CsUserInfo csUserInfo = intelService.interCallPeople(csUserActivity.getUserId());

        CsUserInfoAndIdAndFileInfo userAndActivityIdAndFile = new CsUserInfoAndIdAndFileInfo();
        userAndActivityIdAndFile.setCsUserInfo(csUserInfo);
        userAndActivityIdAndFile.setId(csUserActivity.getId());
        userAndActivityIdAndFile.setFiles(JSON.toJSONString(fileUrl));
        return userAndActivityIdAndFile;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteActivity(CsUserActivityDeleteBo csUserActivityDeleteBo) {
        //删除总表根据三个索引字段删除
        int index = csUserActivityMapper.deleteByIdAndTypeAndStatus(csUserActivityDeleteBo.getActivityId(),
                csUserActivityDeleteBo.getActivityType().code, csUserActivityDeleteBo.getActivityStatus().code);
        log.info("Delete activity [{}] from summary table", csUserActivityDeleteBo.getActivityId());

        //总表删除记录为1
        if (index == 1) {
            //删除的是问题
            if (Objects.equals(csUserActivityDeleteBo.getActivityType().code, ActivityType.QUESTION.code)) {

                //删除问题表
                csUserQuestionMapper.deleteByQuestionId(csUserActivityDeleteBo.getActivityId());
                log.info("Delete question success --- [{}] from question table", csUserActivityDeleteBo.getActivityId());

                //删除的是任务
            } else if (Objects.equals(csUserActivityDeleteBo.getActivityType().code, ActivityType.TASK.code)) {

                //删除数据库
                csUserTaskMapper.deleteByTaskId(csUserActivityDeleteBo.getActivityId());
                log.info("Delete task success --- [{}] from task table", csUserActivityDeleteBo.getActivityId());

                try {
                    //删除缓存
                    String deleteKey = judgeType(csUserActivityDeleteBo.getActivityStatus().code);
                    Set<String> keys = redisOperator.scan(deleteKey);
                    if (keys != null && keys.size() != 0) {
                        redisOperator.delCollect(keys);
                        log.info("delete [{}] from redis success", deleteKey);
                    }
                } catch (Exception e) {
                    log.error("add task del redis error:[{}]", e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }

            ActivityMessage activityMessage = new ActivityMessage(csUserActivityDeleteBo.getActivityId(),
                    csUserActivityDeleteBo.getUserId(), DateUtil.dateToString(new Date()));
            //发送消息
            if (!activitySource.activityOutput().send(
                    MessageBuilder.withPayload(JSON.toJSONString(activityMessage)).build()

            )) {
                throw new RuntimeException("send activity message failure");
            }
            log.info("send activity message success");
        }
    }


    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageActivityFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
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
    public PageActivityInfo getPageActivity(PageInfo pageInfo, String userId) {
        Page<CsUserActivity> activityPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageActivity(activityPage);

        List<CsUserActivity> activities = csUserActivityPage.getRecords();
        log.info("Get all activity -- page [{}] size [{}] data size [{}]", pageInfo.getPage(),
                pageInfo.getSize(), activities.size());

        PageActivityInfo pageActivityInfo = new PageActivityInfo();

        pageActivityInfo.setActivityInfos(getCsActivityVo(activities, userId));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > pageInfo.getPage());
        return pageActivityInfo;
    }

    public PageActivityInfo getPageActivityFallBack(PageInfo pageInfo, String userId, Throwable throwable) {
        log.error("user [{}] get page [{}] size [{}] activity into fallBack method : [{}]", userId, pageInfo.getPage(),
                pageInfo.getSize(), throwable.getMessage());
        return null;
    }

    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageActivityByUserIdFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            }
    )
    @Override
    public PageActivityInfo getPageActivityByUserId(String userId, PageInfo pageInfo) {
        Page<CsUserActivity> activityPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageByUserId(activityPage, userId);

        List<CsUserActivity> activitiesByUserId = csUserActivityPage.getRecords();
        log.info("Get activity by userId [{}] -- page [{}] size [{}] data size [{}] -- [{}]", userId, pageInfo.getPage(),
                pageInfo.getSize(), activitiesByUserId.size(), activitiesByUserId);

        PageActivityInfo pageActivityInfo = new PageActivityInfo();
        pageActivityInfo.setActivityInfos(getCsActivityVo(activitiesByUserId, ME));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > pageInfo.getPage());
        return pageActivityInfo;
    }

    public PageActivityInfo getPageActivityByUserIdFallBack(String userId, PageInfo pageInfo, Throwable throwable) {
        log.error("get  page [{}] size [{}] user [{}] activity into fallBack method : [{}]", pageInfo.getPage()
                , pageInfo.getSize(), userId, throwable.getMessage());
        return null;
    }


    public List<CsUserActivityVo> getCsActivityVo(List<CsUserActivity> activityList, String userId) {
        if (null == activityList || activityList.size() == 0) {
            return null;
        }
        //用户id为了获取用户信息
        Set<String> idList = activityList.stream().map(CsUserActivity::getUserId).collect(Collectors.toSet());

        //动态id为了获取评论信息
        List<String> activityIdList = activityList.stream().map(CsUserActivity::getId).collect(Collectors.toList());

        // 调用用户服务
        Map<String, CsUserInfo> userMap = intelService.interCallPeopleList(idList);

        //调用评论服务//记得让前端搞一下降级的 -1L
        Map<String, Long> commentMap = intelService.interCallComment(activityIdList);

        List<CsUserActivityVo> csActivityVos = new ArrayList<>();

        if (!userId.equals(ME)) {
            activityList.forEach(csActivity -> {
                CsUserActivityVo csActivityVo = new CsUserActivityVo();
                CsUserActivityVo.Activity simpleActivity = CsUserActivityVo.Activity.to(csActivity);
                csActivityVo.setCsUserActivity(simpleActivity);
                csActivityVo.setCommentNumber(commentMap.get(csActivity.getId()));
                csActivityVo.setCsUserInfo(userMap.get(csActivity.getUserId()));
                csActivityVo.setIsModify(userId.equals(csActivity.getUserId()));
                csActivityVos.add(csActivityVo);
            });
        } else {
            activityList.forEach(csActivity -> {
                CsUserActivityVo csActivityVo = new CsUserActivityVo();
                CsUserActivityVo.Activity simpleActivity = CsUserActivityVo.Activity.to(csActivity);
                csActivityVo.setCsUserActivity(simpleActivity);
                csActivityVo.setCommentNumber(commentMap.get(csActivity.getId()));
                csActivityVo.setCsUserInfo(userMap.get(csActivity.getUserId()));
                csActivityVos.add(csActivityVo);
            });
        }

        return csActivityVos;

    }

    /**
     * 判断任务类型
     *
     * @param code doing / did / future
     * @return 存入redis的字符串
     */
    public String judgeType(Integer code) {

        String deleteKey = "";
        if (code.equals(ActivityStatus.ONGOING.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_DOING + "*";
        } else if (code.equals(ActivityStatus.PENDING.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_FUTURE + "*";
        } else if (code.equals(ActivityStatus.ACHIEVE.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_DID + "*";
        }

        return deleteKey;

    }

}
