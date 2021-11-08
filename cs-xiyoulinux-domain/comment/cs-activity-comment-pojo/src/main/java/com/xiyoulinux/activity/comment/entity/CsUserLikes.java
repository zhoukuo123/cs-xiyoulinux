package com.xiyoulinux.activity.comment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户点赞评论记录
 *
 * @author qkm
 * @TableName cs_user_likes
 */
@Data
@TableName(value = "cs_user_likes")
@AllArgsConstructor
@NoArgsConstructor
public class CsUserLikes {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 评论id
     */
    @TableField(value = "cs_comment_id")
    private String csCommentId;

    /**
     * 用户id
     */
    @TableField(value = "cs_user_id")
    private String csUserId;

    /**
     * 动态id
     */
    @TableField(value = "cs_activity_id")
    private String csActivityId;

}