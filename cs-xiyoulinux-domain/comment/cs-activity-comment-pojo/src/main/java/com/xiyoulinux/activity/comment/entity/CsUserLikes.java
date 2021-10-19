package com.xiyoulinux.activity.comment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户点赞评论记录
 *
 * @author qkm
 * @TableName cs_user_likes
 */
@Data
@TableName(value = "cs_user_likes")
@AllArgsConstructor
public class CsUserLikes implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 评论id
     */
    private String csCommentId;

    /**
     * 用户id
     */
    private String csUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}