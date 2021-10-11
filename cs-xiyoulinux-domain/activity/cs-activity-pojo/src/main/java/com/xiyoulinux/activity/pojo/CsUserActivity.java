package com.xiyoulinux.activity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户动态表
 *
 * @author qkm
 * @TableName cs_user_activity
 */
@TableName(value = "cs_user_activity")
@Data
public class CsUserActivity implements Serializable {
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
     * 问题是否解决(0代表未解决)
     */
    @TableField(value = "question_is_solve")
    private Integer questionIsSolve;

    /**
     * 问题or动态or讲座or任务(0/1/2/3)
     */
    @TableField(value = "activity_type")
    private Integer activityType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}