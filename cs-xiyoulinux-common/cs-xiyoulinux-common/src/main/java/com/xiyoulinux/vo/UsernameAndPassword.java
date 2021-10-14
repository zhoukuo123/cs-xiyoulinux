package com.xiyoulinux.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户名和密码(用户登陆注册所需要的)
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登陆和注册的信息")
public class UsernameAndPassword implements Serializable {

    private static final long serialVersionUID = -3650524998125630214L;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;
}
