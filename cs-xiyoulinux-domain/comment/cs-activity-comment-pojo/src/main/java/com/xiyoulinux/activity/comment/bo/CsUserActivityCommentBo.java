package com.xiyoulinux.activity.comment.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户评论动态
 *
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "动态评论对象")
public class CsUserActivityCommentBo implements Serializable {
    private static final long serialVersionUID = -1351309525049849086L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id")
    private String activityId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    /**
     * 评论创建时间
     */
    @ApiModelProperty(value = "评论的创建时间")
    private Date commentCreateTime;

}
