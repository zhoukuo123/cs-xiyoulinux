package com.xiyoulinux.activity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.enums.ActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户动态表
 *
 * @author qkm
 * @TableName cs_user_activity
 */
@Data
@TableName(value = "cs_user_activity")
@AllArgsConstructor
@NoArgsConstructor
public class CsUserActivity {

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 用户Id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 活动的标题
     */
    @TableField(value = "activity_title")
    private String activityTitle;

    /**
     * 问题or动态or讲座or任务的内容
     */
    @TableField(value = "activity_content")
    private String activityContent;

    /**
     * 创建时间
     */
    @TableField(value = "activity_create_time")
    private Date activityCreateTime;

    /**
     * 结束时间
     */
    @TableField(value = "activity_end_time")
    private Date activityEndTime;

    /**
     * 问题or动态or讲座or任务(0/1/2/3)
     * {@link ActivityType}
     */
    @TableField(value = "activity_type")
    private ActivityType activityType;

    /**
     * 任务/讲座/问题的状态
     * {@link ActivityStatus}
     */
    @TableField(value = "activity_status")
    private ActivityStatus activityStatus;

    /**
     * 动态的文件信息
     */
    @TableField(value = "activity_files")
    private String activityFiles;

}