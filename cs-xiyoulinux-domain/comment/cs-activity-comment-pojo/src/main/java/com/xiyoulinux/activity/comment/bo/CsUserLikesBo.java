package com.xiyoulinux.activity.comment.bo;
;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户点赞评论记录
 *
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户点赞对象")
public class CsUserLikesBo implements Serializable {
    private static final long serialVersionUID = -8586434894069292337L;
    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private String csCommentId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String csUserId;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id")
    private String csActivityId;


}