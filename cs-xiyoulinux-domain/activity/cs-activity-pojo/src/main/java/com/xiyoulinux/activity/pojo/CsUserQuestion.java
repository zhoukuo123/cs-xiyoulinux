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
 * 用户问题表
 *
 * @author qkm
 * @TableName cs_user_question
 */
@Data
@TableName(value = "cs_user_question")
@AllArgsConstructor
@NoArgsConstructor
public class CsUserQuestion {

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 问题id
     */
    @TableId(value = "question_id")
    private String questionId;

    /**
     * 用户d
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 问题的内容
     */
    @TableField(value = "question_content")
    private String questionContent;

    /**
     * 创建时间
     */
    @TableField(value = "question_create_time")
    private Date questionCreateTime;

    /**
     * 问题的状态
     * {@link ActivityStatus}
     */
    @TableField(value = "question_status")
    private ActivityStatus questionStatus;

}