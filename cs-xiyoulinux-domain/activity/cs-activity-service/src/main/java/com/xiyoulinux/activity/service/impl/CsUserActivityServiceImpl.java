package com.xiyoulinux.activity.service.impl;

//import com.alibaba.fescar.spring.annotation.GlobalTransactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.constant.TaskConstant;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.bo.CsUserInfoAndId;
import com.xiyoulinux.activity.comment.service.CommentService;
import com.xiyoulinux.activity.inter.InterService;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.mapper.CsUserQuestionMapper;
import com.xiyoulinux.activity.mapper.CsUserTaskMapper;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.activity.pojo.CsUserQuestion;
import com.xiyoulinux.activity.pojo.CsUserTask;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.vo.CsUserActivityVo;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.file.service.DeleteFileService;
import com.xiyoulinux.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import com.xiyoulinux.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.dubbo.config.annotation.DubboReference;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
public class CsUserActivityServiceImpl implements ICsUserActivityService {

    @Resource
    private CsUserActivityMapper csUserActivityMapper;

    @Resource
    private CsUserQuestionMapper csUserQuestionMapper;

    @Resource
    private CsUserTaskMapper csUserTaskMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private InterService interService;

    @Resource
    private Sid sid;

    @DubboReference
    private UploadFileService uploadFileService;

    @DubboReference
    private CommentService commentService;

    @DubboReference
    private DeleteFileService deleteFileService;


    @Override
//    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CsUserInfoAndId addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files) {
        CsUserActivity csUserActivity = new CsUserActivity();
        BeanUtils.copyProperties(csUserActivityBo, csUserActivity);
        //分布式id
        csUserActivity.setId(sid.nextShort());
        csUserActivityMapper.insert(csUserActivity);
        log.info("Insert activity --- [{}]", JSON.toJSONString(csUserActivity));
        if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.QUESTION.code)) {
            CsUserQuestion csUserQuestion = new CsUserQuestion(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityStatus());
            csUserQuestionMapper.insert(csUserQuestion);
            log.info("Insert question --- [{}]", JSON.toJSONString(csUserActivity));
        } else if (Objects.equals(csUserActivity.getActivityType().code, ActivityType.TASK.code)) {
            CsUserTask csUserTask = new CsUserTask(sid.nextShort(), csUserActivity.getId(),
                    csUserActivity.getUserId(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityEndTime(), csUserActivity.getActivityStatus());
            /**
             * TODO 第二次删除缓存
             */
            //先删除缓存，等数据库操作结束之后延迟一段时间（根据从数据库中查询然后放到缓存中的时间）在删除缓存一遍，保证数据库和缓存的一致性。
            String deleteKey = "";
            if (csUserTask.getTaskStatus().code.equals(ActivityStatus.ONGOING.code)) {
                deleteKey = TaskConstant.PAGE_TASK_DOING + "*";
            } else if (csUserTask.getTaskStatus().code.equals(ActivityStatus.PENDING.code)) {
                deleteKey = TaskConstant.PAGE_TASK_FUTURE + "*";
            } else if (csUserTask.getTaskStatus().code.equals(ActivityStatus.ACHIEVE.code)) {
                deleteKey = TaskConstant.PAGE_TASK_DID + "*";
            }
            Set<String> keys = stringRedisTemplate.keys(deleteKey);
            if (keys != null && keys.size() != 0) {
                stringRedisTemplate.delete(keys);
                log.info("delete [{}] from redis", deleteKey);
            }
            csUserTaskMapper.insert(csUserTask);
            log.info("Insert task --- [{}]", JSON.toJSONString(csUserActivity));
        }

        //是否需要捕获异常
        //redis
        //2.新增数据到redis中（不用删除原来缓存存在的，只是从缓存中获取然后添加之后在放回缓存），
        // 问题是多个线程都写，会造成数据不一致的问题。（两个写缓存的线程都读到相同的数据各自分别添加记录在写灰缓存就会造成数据不一致）
        //（用消息队列？串行化更新操作）

        //2.先把缓存全部删除，然后再去加数据库，加数据库结束在删除一次缓存（但是动态是频繁更新的，如果好多人都发动态基本上
        // 都是从数据库里面获取了（采用双删策略）

        //2.加锁？

        //3调用图片服务将图片上传到云服务器以及保存到文件数据库中
        if (files != null) {
            uploadFileService.uploadActivityFile(files, csUserActivity.getId());
        }
        //调用用户中心获取用户信息
        CsUserInfo csUserInfo = interService.interCallPeople(csUserActivity.getUserId());
        CsUserInfoAndId userAndActivityId = new CsUserInfoAndId();
        userAndActivityId.setCsUserInfo(csUserInfo);
        userAndActivityId.setId(new CsUserInfoAndId.Id(csUserActivity.getId()));
        return userAndActivityId;
        //两个事务（1和3）（分布式事务 seata?）
    }

    @Override
//    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteActivity(String id, ActivityType type) {
        csUserActivityMapper.deleteById(id);
        log.info("Delete activity --- [{}] from summary table", id);
        if (Objects.equals(type.getCode(), ActivityType.QUESTION.code)) {
            csUserQuestionMapper.deleteByQuestionId(id);
            log.info("Delete question --- [{}] from question table", id);
        } else if (Objects.equals(type.getCode(), ActivityType.TASK.code)) {
            /**
             * TODO 第二次删除缓存
             */
            //先删除缓存
            String deleteKey = "";
            if (type.code.equals(ActivityStatus.ONGOING.code)) {
                deleteKey = TaskConstant.PAGE_TASK_DOING + "*";
            } else if (type.code.equals(ActivityStatus.PENDING.code)) {
                deleteKey = TaskConstant.PAGE_TASK_FUTURE + "*";
            } else if (type.code.equals(ActivityStatus.ACHIEVE.code)) {
                deleteKey = TaskConstant.PAGE_TASK_DID + "*";
            }
            Set<String> keys = stringRedisTemplate.keys(deleteKey);
            if (keys != null && keys.size() != 0) {
                stringRedisTemplate.delete(keys);
                log.info("delete [{}] from redis", deleteKey);
            }
            //在更新数据库
            csUserTaskMapper.deleteByTaskId(id);
            log.info("Delete task --- [{}] from task table", id);
        }

        //2.多个线程同时删除数据缓存的数据，导致缓存的数据不一致问题（先删除数据库，再去删除缓存）使用消息队列串行化更新操作？
        //因为删除数据是先删除数据库，在删除缓存中的部分数据，在删除缓存中的部分数据时，会造成缓存数据的错误（都先获取到相同的数据
        // 然后各自删除记录，之后在存入缓存就会造成不一致）

        //2.全部删除，然后下次在从数据里面获取（采用双删策略）

        //2.加锁？
        //3是否删除？

        //删除动态底下的所有评论
        commentService.deleteComments(id);

        //删除动态的文件信息以及所有评论的文件信息
        deleteFileService.deleteActivityAndCommentFile(id);

    }

    @Override
//    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
    public PageActivityInfo getPageActivity(int page) {
        Page<CsUserActivity> activityPage = new Page<>(page, 20);
        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageActivity(activityPage);
        List<CsUserActivity> activities = csUserActivityPage.getRecords();
        log.info("Get all activity -- page [{}] -- [{}]", page, activities);
        PageActivityInfo pageActivityInfo = new PageActivityInfo();
        pageActivityInfo.setActivityInfos(getCsActivityVo(activities));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > page);
        return pageActivityInfo;
    }


    @Override
//    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
    public PageActivityInfo getPageActivityByUserId(String userId, int page) {
        Page<CsUserActivity> activityPage = new Page<>(page, 10);
        IPage<CsUserActivity> csUserActivityPage = csUserActivityMapper.selectPageByUserId(activityPage, userId);
        List<CsUserActivity> activitiesByUserId = csUserActivityPage.getRecords();
        log.info("Get activity by userId [{}] -- page [{}] -- [{}]", userId, page, activitiesByUserId);
        PageActivityInfo pageActivityInfo = new PageActivityInfo();
        pageActivityInfo.setActivityInfos(getCsActivityVo(activitiesByUserId));
        pageActivityInfo.setHasMore(csUserActivityPage.getPages() > page);
        return pageActivityInfo;
    }

    //    @GlobalTransactional
    public List<CsUserActivityVo> getCsActivityVo(List<CsUserActivity> activityList) {
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
        //调用评论服务
        Map<String, Long> commentMap = interService.interCallComment(activityIdList);

        //返回动态vo对象
        List<CsUserActivityVo> csActivityVos = new ArrayList<>();
        activityList.forEach(csActivity -> {
            CsUserActivityVo csActivityVo = new CsUserActivityVo();
            CsUserActivityVo.Activity simpleActivity = CsUserActivityVo.Activity.to(csActivity);
            csActivityVo.setCsUserActivity(simpleActivity);
            csActivityVo.setActivityPicturesUrl(fileUrlByActivityIdMap == null ? null : fileUrlByActivityIdMap.get(csActivity.getId()));
            csActivityVo.setCommentNumber(commentMap == null ? null : commentMap.get(csActivity.getId()));
            csActivityVo.setCsUserInfo(userMap == null ? null : userMap.get(csActivity.getUserId()));
            csActivityVos.add(csActivityVo);
        });
        return csActivityVos;
    }

}




