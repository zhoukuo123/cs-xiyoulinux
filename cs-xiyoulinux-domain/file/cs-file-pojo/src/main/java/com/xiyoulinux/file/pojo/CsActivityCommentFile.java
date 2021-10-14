package com.xiyoulinux.file.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qkm
 * @TableName cs_activity_comment_file
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论的文件对象")
public class CsActivityCommentFile implements Serializable {
    private static final long serialVersionUID = -5183853457140866867L;
    /**
     * 主键id
     */

    @TableField(value = "id")
    @ApiModelProperty("")
    private String id;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private String commentId;

    /**
     * 评论内容包含的文件url
     */
    @TableField(value = "comment_file_url")
    private String commentFileUrl;

    /**
     * 动态id
     */
    @TableField(value = "activity_id")
    private String activityId;
}