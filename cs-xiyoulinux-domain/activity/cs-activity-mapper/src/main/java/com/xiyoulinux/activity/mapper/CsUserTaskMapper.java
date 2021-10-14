package com.xiyoulinux.activity.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiyoulinux.activity.entity.CsUserTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.enums.ActivityStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author qkm
 * @Entity com.xiyoulinux.activity.pojo.CsUserTask
 */
public interface CsUserTaskMapper extends BaseMapper<CsUserTask> {

    /**
     * 分页获取进行中的任务数
     *
     * @param page 分页
     * @return 进行中的任务集合
     */
    IPage<CsUserTask> getPageDoingTasks(@Param("page") IPage<CsUserTask> page);


    /**
     * 分页获取已完成的任务数
     *
     * @param page 分页
     * @return 已完成的任务集合
     */
    IPage<CsUserTask> getPageDidTasks(@Param("page") IPage<CsUserTask> page);


    /**
     * 分页获取待进行的任务数
     *
     * @param page 分页
     * @return 待进行的任务集合
     */
    IPage<CsUserTask> getPageFutureTasks(@Param("page") IPage<CsUserTask> page);

    /**
     * 更新任务的结束时间
     *
     * @param id          任务id
     * @param taskEndTime 要更新的任务结束时间
     * @param taskStatus  要更新的任务的状态
     */
    void updateEndTimeAndStatusByTaskId(@Param("id") String id, @Param("taskEndTime") Date taskEndTime,
                                        @Param("taskStatus") ActivityStatus taskStatus);

    /**
     * 根据任务id删除
     *
     * @param taskId 任务id
     */
    void deleteByTaskId(@Param("taskId") String taskId);

}




