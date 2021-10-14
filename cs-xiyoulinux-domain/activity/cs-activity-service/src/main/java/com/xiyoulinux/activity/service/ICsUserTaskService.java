package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.vo.PageTaskInfo;
import com.xiyoulinux.enums.ActivityStatus;

import java.util.Date;

/**
 * @author qkm
 */
public interface ICsUserTaskService {

    /**
     * 分页获取进行中的任务数
     *
     * @param page 第几页
     * @return 进行中的任务集合
     */
     PageTaskInfo getPageDoingTasks(int page);

    /**
     * 分页获取已完成的任务数
     *
     * @param page 第几页
     * @return 已完成的任务集合
     */
     PageTaskInfo getPageDidTasks(int page);


    /**
     * 分页获取待进行的任务数
     *
     * @param page 第几页
     * @return 待进行的任务集合
     */
     PageTaskInfo getPageFutureTasks(int page);

    /**
     * 管理员更新任务信息
     *
     * @param id          任务的主键id
     * @param taskEndTime 更新任务的结束时间
     * @param taskStatus  更新任务的状态
     */
     void updateTasks(String id, Date taskEndTime, ActivityStatus taskStatus);

}
