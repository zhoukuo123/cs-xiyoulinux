package com.xiyoulinux.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyoulinux.activity.constant.TaskConstant;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.vo.PageTaskInfo;
import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.activity.inter.InterService;
import com.xiyoulinux.activity.mapper.CsUserTaskMapper;
import com.xiyoulinux.activity.pojo.CsUserTask;
import com.xiyoulinux.activity.service.ICsUserTaskService;
import com.xiyoulinux.activity.vo.CsUserTaskVo;
import com.xiyoulinux.enums.ActivityStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Slf4j
@Service
public class CsUserTaskServiceImpl implements ICsUserTaskService {
    @Resource
    private CsUserActivityMapper csUserActivityMapper;
    @Resource
    private CsUserTaskMapper csUserTaskMapper;

    @Resource
    private InterService interService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public PageTaskInfo getPageDoingTasks(int page) {
        return getPageTaskInfo(page,ActivityStatus.ONGOING);
    }

    @Override
    public PageTaskInfo getPageDidTasks(int page) {
        return getPageTaskInfo(page,ActivityStatus.ACHIEVE);
    }

    @Override
    public PageTaskInfo getPageFutureTasks(int page) {
        return getPageTaskInfo(page,ActivityStatus.PENDING);
    }

    @Override
    public void updateTasks(String id, Date taskEndTime, ActivityStatus taskStatus) {
        /**
         * TODO 第二次删除缓存
         */
        //先删除缓存
        String deleteKey = "";
        if (taskStatus.code.equals(ActivityStatus.ONGOING.code)) {
            deleteKey = TaskConstant.PAGE_TASK_DOING + "*";
        } else if (taskStatus.code.equals(ActivityStatus.PENDING.code)) {
            deleteKey = TaskConstant.PAGE_TASK_FUTURE + "*";
        } else if (taskStatus.code.equals(ActivityStatus.ACHIEVE.code)) {
            deleteKey = TaskConstant.PAGE_TASK_DID + "*";
        }
        Set<String> keys = stringRedisTemplate.keys(deleteKey);
        if (keys != null && keys.size() != 0) {
            stringRedisTemplate.delete(keys);
            log.info("delete [{}] from redis", deleteKey);
        }
        //在更新数据库
        csUserActivityMapper.updateEndTimeAndStatusById(id, taskEndTime, taskStatus);
        csUserTaskMapper.updateEndTimeAndStatusByTaskId(id, taskEndTime, taskStatus);
    }

    private List<CsUserTaskVo> getCsTaskVo(List<CsUserTask> taskList) {
        if (null == taskList || taskList.size() == 0) {
            return null;
        }
        Set<String> idList = taskList.stream().map(CsUserTask::getUserId).collect(Collectors.toSet());
        log.info("Get task --- get userId [{}]", JSON.toJSONString(idList));

        List<String> taskIdList = taskList.stream().map(CsUserTask::getTaskId).collect(Collectors.toList());
        log.info("Get task --- get taskId [{}]", JSON.toJSONString(taskIdList));

        log.info("Get task --- get userInfo....");
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);

        log.info("Get task --- get files....");
        Map<String, List<String>> fileUrlByActivityIdMap = interService.interCallFile(taskIdList);

        log.info("Get task --- get comment....");
        Map<String, Long> commentMap = interService.interCallComment(taskIdList);
        List<CsUserTaskVo> csUserTaskVos = new ArrayList<>();
        taskList.forEach(csUserTask -> {
            CsUserTaskVo csUserTaskVo = new CsUserTaskVo();
            CsUserTaskVo.Task simpleTask = CsUserTaskVo.Task.to(csUserTask);
            csUserTaskVo.setCsUserTask(simpleTask);
            csUserTaskVo.setCsUserInfo(userMap == null ? null : userMap.get(csUserTask.getUserId()));
            csUserTaskVo.setTaskPicturesUrl(fileUrlByActivityIdMap == null ?
                    null : fileUrlByActivityIdMap.get(csUserTask.getTaskId()));
            csUserTaskVo.setCommentNumber(commentMap == null ? null : commentMap.get(csUserTask.getTaskId()));
            csUserTaskVos.add(csUserTaskVo);
        });
        return csUserTaskVos;
    }


    private PageTaskInfo getPageTaskInfo(int page, ActivityStatus activityStatus) {
        PageTaskInfo pageTaskInfo = new PageTaskInfo();
        String key = "";
        if (activityStatus.code.equals(ActivityStatus.ONGOING.code)) {
            key = TaskConstant.PAGE_TASK_DOING + page;
        } else if (activityStatus.code.equals(ActivityStatus.PENDING.code)) {
            key = TaskConstant.PAGE_TASK_FUTURE + page;
        } else {
            key = TaskConstant.PAGE_TASK_DID + page;
        }
        //先从缓存里面查找
        List<Object> pageTaskInfoRedis = stringRedisTemplate.opsForHash().
                multiGet(key, Arrays.asList("value", "hasMore"))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //不为空
        if (CollectionUtils.isNotEmpty(pageTaskInfoRedis)) {
            //包装包装返回对象
            pageTaskInfo.setTaskInfos(JSON.parseArray(pageTaskInfoRedis.get(0).toString(), CsUserTaskVo.class));
            pageTaskInfo.setHasMore(JSON.parseObject(pageTaskInfoRedis.get(1).toString(), Boolean.class));
            log.info("Get [{}] task from redis --- page [{}] -- [{}]", activityStatus.description,page, pageTaskInfo.getTaskInfos());
            //直接从缓存里面获取到然后返回
            return pageTaskInfo;
        }

        //从数据库中查询List<CsUserTask>
        Page<CsUserTask> taskPage = new Page<>(page, 20);

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
        List<CsUserTaskVo> csTaskVo = getCsTaskVo(task);
        //是否还有下一页
        boolean hasMore = pageTasks.getPages() > page;
        log.info("Get [{}] task from db -- page [{}] -- [{}]", activityStatus.description, page, task);

        //存入缓存
        stringRedisTemplate.opsForHash().put(key, "value", JSON.toJSONString(csTaskVo));
        stringRedisTemplate.opsForHash().put(key, "hasMore", JSON.toJSONString(hasMore));
        log.info("select from db to save redis -- page [{}] [{}] task [{}]",  page,activityStatus.description, task);

        //包装返回对象
        pageTaskInfo.setTaskInfos(csTaskVo);
        pageTaskInfo.setHasMore(hasMore);
        return pageTaskInfo;
    }
}




