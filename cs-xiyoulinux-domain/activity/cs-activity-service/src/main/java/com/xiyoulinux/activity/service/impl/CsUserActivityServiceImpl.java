package com.xiyoulinux.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.constant.ActivityConstant;
import com.xiyoulinux.activity.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.activity.comment.service.CommentService;
import com.xiyoulinux.activity.inter.InterService;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.mapper.CsUserTaskMapper;
import com.xiyoulinux.activity.entity.CsUserActivity;
import com.xiyoulinux.activity.entity.CsUserQuestion;
import com.xiyoulinux.activity.entity.CsUserTask;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.vo.CsUserActivityVo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.file.service.DeleteFileService;
import com.xiyoulinux.file.service.UploadFileService;
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
public class CsUserActivityServiceImpl implements ICsUserActivityService {

    private final CsUserActivityMapper csUserActivityMapper;

    private final CsUserQuestionMapper csUserQuestionMapper;

    private final CsUserTaskMapper csUserTaskMapper;

    private final RedisOperator redisOperator;

    private final InterService interService;

    private final Sid sid;

    @DubboReference
    private UploadFileService uploadFileService;

    @DubboReference
    private CommentService commentService;

    @DubboReference
    private DeleteFileService deleteFileService;

    public CsUserActivityServiceImpl(CsUserActivityMapper csUserActivityMapper,
                                     CsUserQuestionMapper csUserQuestionMapper,
                                     CsUserTaskMapper csUserTaskMapper,
                                     RedisOperator redisOperator,
                                     InterService interService,
                                     Sid sid) {
        this.csUserActivityMapper = csUserActivityMapper;
        this.csUserQuestionMapper = csUserQuestionMapper;
        this.csUserTaskMapper = csUserTaskMapper;
        this.redisOperator = redisOperator;
        this.interService = interService;
        this.sid = sid;
    }


    @Override
    @GlobalTransactional
//    public CsUserInfoAndId addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files) {
    public CsUserInfoAndIdAndFileInfo addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files) {
        CsUserActivity csUserActivity = new CsUserActivity();
        BeanUtils.copyProperties(csUserActivityBo, csUserActivity);
        //分布式id
        csUserActivity.setId(sid.nextShort());

        //插入总表
        csUserActivityMapper.insert(csUserActivity);
        log.info("Insert activity --- [{}]", JSON.toJSONString(csUserActivity));

        //插入问题表
        if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.QUESTION.code)) {
            CsUserQuestion csUserQuestion = new CsUserQuestion(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityTitle(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityStatus());

            csUserQuestionMapper.insert(csUserQuestion);
            log.info("Insert question to db --- [{}]", JSON.toJSONString(csUserActivity));
            //插入任务表
        } else if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.TASK.code)) {
            CsUserTask csUserTask = new CsUserTask(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityTitle(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityEndTime(), csUserActivity.getActivityStatus());
            /**
             *  TODO 第二次删除缓存
             */
            //先删除缓存，等数据库操作结束之后延迟一段时间（根据从数据库中查询然后放到缓存中的时间）在删除缓存一遍，保证数据库和缓存的一致性。
            //获取redis中存储任务的key
            String deleteKey = judgeType(csUserActivity.getActivityStatus().code);

            //根据deleteKey先删除redis中存储的任务
            Set<String> keys = redisOperator.keys(deleteKey);
            if (keys != null && keys.size() != 0) {
                redisOperator.delCollect(keys);
                log.info("delete [{}] from redis", deleteKey);
            }
            //在传插入数据库
            csUserTaskMapper.insert(csUserTask);
            log.info("Insert task to db --- [{}]", JSON.toJSONString(csUserActivity));
        }

        //调用图片服务将图片上传到云服务器以及保存到文件数据库中
        List<String> fileUrl = uploadFileService.uploadActivityFile(files, csUserActivity.getId());

        //用户服务增加降级---如果前面出现异常则回滾，否则执行到此处，如果调用用户服务有问题，则降级就是说用户服务获取信息使用降级后的，然后返回，
        //也不需要回滾
        CsUserInfo csUserInfo = interService.interCallPeople(csUserActivity.getUserId());
        CsUserInfoAndIdAndFileInfo userAndActivityIdAndFile = new CsUserInfoAndIdAndFileInfo();
        userAndActivityIdAndFile.setCsUserInfo(csUserInfo);
        userAndActivityIdAndFile.setId(csUserActivity.getId());
        userAndActivityIdAndFile.setFiles(fileUrl);
        return userAndActivityIdAndFile;
    }

    @Override
    @GlobalTransactional
    public void deleteActivity(String id, ActivityType type, ActivityStatus status) {
        //删除总表根据三个索引字段删除,防止截包修改内容
        int index = csUserActivityMapper.deleteByIdAndTypeAndStatus(id, type.code, status.code);
        log.info("Delete activity --- [{}] from summary table", id);

        //总表删除记录为1
        if (index == 1) {
            //删除的是问题
            if (Objects.equals(type.getCode(), ActivityType.QUESTION.code)) {

                //删除问题表
                csUserQuestionMapper.deleteByQuestionId(id);
                log.info("Delete question --- [{}] from question table", id);

                //删除的是任务
            } else if (Objects.equals(type.getCode(), ActivityType.TASK.code)) {

                /**
                 * TODO 第二次删除缓存
                 */
                //先删除缓存中存储的任务
                String deleteKey = judgeType(status.code);
                Set<String> keys = redisOperator.keys(deleteKey);
                if (keys != null && keys.size() != 0) {
                    redisOperator.delCollect(keys);
                    log.info("delete [{}] from redis", deleteKey);
                }

                //在更新数据库
                csUserTaskMapper.deleteByTaskId(id);
                log.info("Delete task --- [{}] from task table", id);
            }

            //删除动态底下的所有评论
            commentService.deleteComments(id);

            //删除动态的文件信息以及所有评论的文件信息
            deleteFileService.deleteActivityAndCommentFile(id);
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
    public PageActivityInfo getPageActivity(int page, String userId) {
        Page<CsUserActivity> activityPage = new Page<>(page, 20);

        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageActivity(activityPage);

        List<CsUserActivity> activities = csUserActivityPage.getRecords();
        log.info("Get all activity -- page [{}] -- [{}]", page, activities);

        PageActivityInfo pageActivityInfo = new PageActivityInfo();

        pageActivityInfo.setActivityInfos(getCsActivityVo(activities, userId));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > page);
        return pageActivityInfo;
    }

    public PageActivityInfo getPageActivityFallBack(int page, String userId) {
        log.error("user [{}] get page [{}] activity into fallBack method", userId, page);
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
                            value = "1500")
            }
    )
    @Override
    public PageActivityInfo getPageActivityByUserId(String userId, int page) {
        Page<CsUserActivity> activityPage = new Page<>(page, 10);
        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageByUserId(activityPage, userId);

        List<CsUserActivity> activitiesByUserId = csUserActivityPage.getRecords();
        log.info("Get activity by userId [{}] -- page [{}] -- [{}]", userId, page, activitiesByUserId);

        PageActivityInfo pageActivityInfo = new PageActivityInfo();
        pageActivityInfo.setActivityInfos(getCsActivityVo(activitiesByUserId, userId));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > page);
        return pageActivityInfo;
    }

    public PageActivityInfo getPageActivityByUserIdFallBack(String userId, int page) {
        log.error("get page [{}] user [{}] activity into fallBack method", page, userId);
        return null;
    }


    public List<CsUserActivityVo> getCsActivityVo(List<CsUserActivity> activityList, String userId) {
        if (null == activityList || activityList.size() == 0) {
            return null;
        }
        //用户id为了获取用户信息
        Set<String> idList = activityList.stream().map(CsUserActivity::getUserId).collect(Collectors.toSet());
        log.info("Get activity --- get userId [{}]", JSON.toJSONString(idList));

        //动态id为了获取文件信息
        List<String> activityIdList = activityList.stream().map(CsUserActivity::getId).collect(Collectors.toList());
        log.info("Get activity --- get activityId [{}]", JSON.toJSONString(activityIdList));


        log.info("Get activity --- get userInfo....");
        //调用用户服务
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);

        log.info("Get activity --- get files....");
        //调用文件服务
        Map<String, List<String>> fileUrlByActivityIdMap = interService.interCallFile(activityIdList);


        log.info("Get activity --- get comment....");
        //调用评论服务//记得让前端搞一下降级的 -1L
        Map<String, Long> commentMap = interService.interCallComment(activityIdList);


        List<CsUserActivityVo> csActivityVos = new ArrayList<>();

        activityList.forEach(csActivity -> {
            CsUserActivityVo csActivityVo = new CsUserActivityVo();
            CsUserActivityVo.Activity simpleActivity = CsUserActivityVo.Activity.to(csActivity);
            csActivityVo.setCsUserActivity(simpleActivity);
            csActivityVo.setActivityFilesUrl(fileUrlByActivityIdMap.get(csActivity.getId()));
            csActivityVo.setCommentNumber(commentMap.get(csActivity.getId()));
            csActivityVo.setCsUserInfo(userMap.get(csActivity.getUserId()));
            csActivityVo.setIsModify(userId.equals(csActivity.getUserId()));
            csActivityVos.add(csActivityVo);
        });
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
