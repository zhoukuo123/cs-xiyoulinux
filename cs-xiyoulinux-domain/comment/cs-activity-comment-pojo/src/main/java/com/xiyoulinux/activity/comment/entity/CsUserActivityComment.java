package com.xiyoulinux.activity.comment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    @TableField(value = "user_id")
    private String userId;

    /**
     * 动态id
     */
    @TableField(value = "activity_id")
    private String activityId;

    /**
     * 评论内容
     */
    @TableField(value = "comment_content")
    private String commentContent;

    /**
     * 评论点赞数目
     */
    @TableField(value = "comment_likes")
    private int commentLikes;

    /**
     * 评论创建时间
     */
    @TableField(value = "comment_create_time")
    private Date commentCreateTime;

    /**
     * 评论对应的文件信息
     */
    @TableField(value = "comment_files")
    private String commentFiles;
}
