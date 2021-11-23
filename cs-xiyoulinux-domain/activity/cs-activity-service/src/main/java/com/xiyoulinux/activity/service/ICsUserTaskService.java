package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.bo.CsUserTaskUpdateBo;
import com.xiyoulinux.activity.vo.PageTaskInfo;
import com.xiyoulinux.activity.vo.TaskNumber;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ActivityStatus;

/**
 * @author qkm
 */
public interface ICsUserTaskService {

    /**
     * 分页获取进行中的任务数
     *
     * @param pageInfo 第几页
     * @param userId   用户id
     * @return 进行中的任务集合 {@link PageTaskInfo}
     */
    PageTaskInfo getPageDoingTasks(PageInfo pageInfo, ActivityStatus activityStatus, String userId);

    /**
     * 分页获取已完成的任务数
     *
     * @param pageInfo 第几页
     * @param userId   用户id
     * @return 已完成的任务集合 {@link PageTaskInfo}
     */
    PageTaskInfo getPageDidTasks(PageInfo pageInfo, ActivityStatus activityStatus,String userId);


    /**
     * 分页获取待进行的任务数
     *
     * @param pageInfo 第几页
     * @param userId   用户id
     * @return 待进行的任务集合 {@link PageTaskInfo}
     */
    PageTaskInfo getPageFutureTasks(PageInfo pageInfo,ActivityStatus activityStatus, String userId);

    /**
     * 管理员更新任务信息
     *
     * @param csUserTaskUpdateBo 更新的任务信息
     */
    void updateTasks(CsUserTaskUpdateBo csUserTaskUpdateBo);

    /**
     * 获取各个任务数量
     *
     * @return 任务总数对象 {@link TaskNumber}
     */
    TaskNumber getTaskNumber();

}
