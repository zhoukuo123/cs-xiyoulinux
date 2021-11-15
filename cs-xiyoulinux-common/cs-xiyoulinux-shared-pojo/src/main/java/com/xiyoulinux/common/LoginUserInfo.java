package com.xiyoulinux.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息 存储在token中的
 *
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "token中存储的用户对象")
public class LoginUserInfo implements Serializable {

    private static final long serialVersionUID = -8688130033155509959L;
    /**
     * 用户 id
     */
    @ApiModelProperty(value = "用户id")
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

}
