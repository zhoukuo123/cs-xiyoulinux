package com.xiyoulinux.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.bo.CsUserTaskUpdateBo;
import com.xiyoulinux.activity.constant.ActivityConstant;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.vo.PageTaskInfo;
import com.xiyoulinux.activity.vo.TaskNumber;
import com.xiyoulinux.activity.inter.IntelService;
import com.xiyoulinux.activity.mapper.CsUserTaskMapper;
import com.xiyoulinux.activity.entity.CsUserTask;
import com.xiyoulinux.activity.service.ICsUserTaskService;
import com.xiyoulinux.activity.vo.CsUserTaskVo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Slf4j
@Service
public class CsUserTaskServiceImpl implements ICsUserTaskService {

    private final CsUserActivityMapper csUserActivityMapper;

    private final CsUserTaskMapper csUserTaskMapper;

    private final IntelService interService;

    private final RedisOperator redisOperator;


    @Autowired
    public CsUserTaskServiceImpl(CsUserActivityMapper csUserActivityMapper,
                                 CsUserTaskMapper csUserTaskMapper,
                                 IntelService interService,
                                 RedisOperator redisOperator) {
        this.csUserTaskMapper = csUserTaskMapper;
        this.interService = interService;
        this.redisOperator = redisOperator;
        this.csUserActivityMapper = csUserActivityMapper;
    }


    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageTaskInfoFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            }
    )
    @Override
    public PageTaskInfo getPageDoingTasks(PageInfo pageInfo, ActivityStatus activityStatus, String userId) {
        return getPageTaskInfo(pageInfo, activityStatus, userId);
    }

    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageTaskInfoFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            }
    )
    @Override
    public PageTaskInfo getPageDidTasks(PageInfo pageInfo, ActivityStatus activityStatus, String userId) {
        return getPageTaskInfo(pageInfo, activityStatus, userId);
    }

    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getPageTaskInfoFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            }
    )
    @Override
    public PageTaskInfo getPageFutureTasks(PageInfo pageInfo, ActivityStatus activityStatus, String userId) {
        return getPageTaskInfo(pageInfo, activityStatus, userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateTasks(CsUserTaskUpdateBo csUserTaskUpdateBo) {
        //更新数据库
        csUserActivityMapper.updateEndTimeAndStatusById(
                csUserTaskUpdateBo.getTaskId()
                , csUserTaskUpdateBo.getTaskStartTimeDate()
                , csUserTaskUpdateBo.getTaskEndTimeDate(),
                csUserTaskUpdateBo.getTaskStatus());
        csUserTaskMapper.updateEndTimeAndStatusByTaskId(
                csUserTaskUpdateBo.getTaskId()
                , csUserTaskUpdateBo.getTaskStartTimeDate()
                , csUserTaskUpdateBo.getTaskEndTimeDate(),
                csUserTaskUpdateBo.getTaskStatus());

        //删除缓存
        String deleteKey = "";
        if (csUserTaskUpdateBo.getTaskStatus().code.equals(ActivityStatus.ONGOING.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_DOING + "*";
        } else if (csUserTaskUpdateBo.getTaskStatus().code.equals(ActivityStatus.PENDING.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_FUTURE + "*";
        } else if (csUserTaskUpdateBo.getTaskStatus().code.equals(ActivityStatus.ACHIEVE.code)) {
            deleteKey = ActivityConstant.PAGE_TASK_DID + "*";
        }
        Set<String> keys = redisOperator.scan(deleteKey);
        if (keys != null && keys.size() != 0) {
            redisOperator.delCollect(keys);
            log.info("delete [{}] from redis", deleteKey);
        }
    }


    @HystrixCommand(
            groupKey = "activity",
            // 舱壁模式
            threadPoolKey = "activity",
            // 后备模式
            fallbackMethod = "getTaskNumberFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000")
            }

    )
    @Override
    public TaskNumber getTaskNumber() {
        Integer doingNumber = csUserTaskMapper.selectCount(new QueryWrapper<CsUserTask>().eq("task_status", 2));
        Integer futureNumber = csUserTaskMapper.selectCount(new QueryWrapper<CsUserTask>().eq("task_status", 3));
        Integer didNumber = csUserTaskMapper.selectCount(new QueryWrapper<CsUserTask>().eq("task_status", 4));
        log.info("find task number from db, doing [{}]、did [{}]、future [{}]", doingNumber, didNumber, futureNumber);
        return new TaskNumber(doingNumber, futureNumber, didNumber);
    }

    //前端搞一下降级的显示
    public TaskNumber getTaskNumberFallBack(Throwable throwable) {
        log.error("get task number into fallback method : [{}]", throwable.getMessage());
        return new TaskNumber(-1, -1, -1);
    }


    private List<CsUserTaskVo> getCsTaskVo(List<CsUserTask> taskList, String userId) {
        if (null == taskList || taskList.size() == 0) {
            return null;
        }
        Set<String> idList = taskList.stream().map(CsUserTask::getUserId).collect(Collectors.toSet());

        List<String> taskIdList = taskList.stream().map(CsUserTask::getTaskId).collect(Collectors.toList());

        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);

        Map<String, Long> commentMap = interService.interCallComment(taskIdList);

        List<CsUserTaskVo> csUserTaskVos = new ArrayList<>();
        taskList.forEach(csUserTask -> {
            CsUserTaskVo csUserTaskVo = new CsUserTaskVo();
            CsUserTaskVo.Task simpleTask = CsUserTaskVo.Task.to(csUserTask);
            csUserTaskVo.setCsUserTask(simpleTask);
            csUserTaskVo.setCsUserInfo(userMap.get(csUserTask.getUserId()));
            csUserTaskVo.setCommentNumber(commentMap.get(csUserTask.getTaskId()));
            System.out.println(csUserTask.getUserId());
            csUserTaskVo.setIsModify(userId.equals(csUserTask.getUserId()));
            csUserTaskVos.add(csUserTaskVo);
        });
        return csUserTaskVos;
    }


    private PageTaskInfo getPageTaskInfo(PageInfo pageInfo, ActivityStatus activityStatus, String userId) {
        PageTaskInfo pageTaskInfo = new PageTaskInfo();
        String key = "";
        if (activityStatus.code.equals(ActivityStatus.ONGOING.code)) {
            key = ActivityConstant.PAGE_TASK_DOING + pageInfo.getPage();
        } else if (activityStatus.code.equals(ActivityStatus.PENDING.code)) {
            key = ActivityConstant.PAGE_TASK_FUTURE + pageInfo.getPage();
        } else {
            key = ActivityConstant.PAGE_TASK_DID + pageInfo.getPage();
        }
        //先从缓存里面查找
        String value = redisOperator.hget(key, "value");
        String hasMoreForRedis = redisOperator.hget(key, "hasMore");

        //不为空
        if (value != null && hasMoreForRedis != null) {
            //包装包装返回对象
            List<CsUserTaskVo> csUserTaskVos = JSON.parseArray(value, CsUserTaskVo.class);
            ArrayList<String> taskIdList = new ArrayList<>();
            csUserTaskVos.forEach(csUserTaskVo -> {
                taskIdList.add(csUserTaskVo.getCsUserTask().getTaskId());
            });
            try {
                Map<String, Long> commentMap = interService.interCallComment(taskIdList);
                csUserTaskVos.forEach(csUserTaskVo -> {
                    csUserTaskVo.setCommentNumber(commentMap.get(csUserTaskVo.getCsUserTask().getTaskId()));
                });
            } catch (RpcException e) {
                log.error("comment server error: [{}]", e.getMessage());
            }
            pageTaskInfo.setTaskInfos(csUserTaskVos);
            pageTaskInfo.setHasMore(JSON.parseObject(hasMoreForRedis, Boolean.class));
            log.info("Get [{}] task from redis --- page [{}] size[{}]", activityStatus.description,
                    pageInfo.getPage(), pageInfo.getSize());
            //直接从缓存里面获取到然后返回
            return pageTaskInfo;
        }

        //从数据库中查询List<CsUserTask>
        Page<CsUserTask> taskPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());

        IPage<CsUserTask> pageTasks = null;
        if (activityStatus.code.equals(ActivityStatus.ONGOING.code)) {
            pageTasks = csUserTaskMapper.getPageDoingTasks(taskPage);
        } else if (activityStatus.code.equals(ActivityStatus.PENDING.code)) {
            pageTasks = csUserTaskMapper.getPageFutureTasks(taskPage);
        } else {
            pageTasks = csUserTaskMapper.getPageDidTasks(taskPage);
        }
        List<CsUserTask> task = pageTasks.getRecords();

        //包装 List<CsUserTaskVo>
        List<CsUserTaskVo> csTaskVo = getCsTaskVo(task, userId);
        //是否还有下一页
        boolean hasMoreForDb = pageTasks.getPages() > pageInfo.getPage();
        log.info("Get [{}] task from db -- page [{}] size [{}]", activityStatus.description,
                pageInfo.getPage(), pageInfo.getSize());

        //存入缓存
        redisOperator.hset(key, "value", JSON.toJSONString(csTaskVo));
        redisOperator.hset(key, "hasMore", JSON.toJSONString(hasMoreForDb));
        log.info("select from db to save redis -- page [{}] size [{}] [{}] task", pageInfo.getPage(),
                pageInfo.getSize(), activityStatus.description);

        //包装返回对象
        pageTaskInfo.setTaskInfos(csTaskVo);
        pageTaskInfo.setHasMore(hasMoreForDb);
        return pageTaskInfo;
    }

    private PageTaskInfo getPageTaskInfoFallBack(PageInfo pageInfo, ActivityStatus activityStatus, String userId, Throwable throwable) {
        log.error("user [{}] get page [{}] [{}] task fallBack method : [{}]", userId,
                pageInfo.getPage(), activityStatus.description, throwable.getMessage()
        );
        return null;
    }
}