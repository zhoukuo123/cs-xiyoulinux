package com.xiyoulinux.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qkm
 * 从用户中心根据用户id获取名字和图像信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息")
public class CsUserInfo implements Serializable {
    private static final long serialVersionUID = -4687621298532390314L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户图片
     */
    @ApiModelProperty(value = "用户图片")
    private String userPic;

}
