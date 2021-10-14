package com.xiyoulinux.activity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiyoulinux.enums.ActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


/**
 * @author qkm
 * @TableName cs_user_task
 */
@Data
@TableName(value = "cs_user_task")
@AllArgsConstructor
@NoArgsConstructor
public class CsUserTask {

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 任务id
     */
    @TableId(value = "task_id")
    private String taskId;

    /**
     * 用户Id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 任务的内容
     */
    @TableField(value = "task_content")
    private String taskContent;

    /**
     * 创建时间
     */
    @TableField(value = "task_create_time")
    private Date taskCreateTime;

    /**
     * 任务的结束时间
     */
    @TableField(value = "task_end_time")
    private Date taskEndTime;

    /**
     * 任务状态
     * {@link ActivityStatus}
     */
    @TableField(value = "task_status")
    private ActivityStatus taskStatus;

}