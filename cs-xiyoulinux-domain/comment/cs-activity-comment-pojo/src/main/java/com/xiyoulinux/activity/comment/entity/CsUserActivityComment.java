package com.xiyoulinux.activity.comment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户评论动态
 *
 * @author qkm
 * @TableName cs_user_activity_comment
 */
@TableName(value = "cs_user_activity_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsUserActivityComment {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 用户id
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 动态id
     */
    @TableId(value = "activity_id")
    private String activityId;

    /**
     * 评论内容
     */
    @TableId(value = "comment_content")
    private String commentContent;

    /**
     * 评论点赞数目
     */
    @TableId(value = "comment_likes")
    private int commentLikes;

    /**
     * 评论创建时间
     */
    @TableId(value = "comment_create_time")
    private Date commentCreateTime;
}
