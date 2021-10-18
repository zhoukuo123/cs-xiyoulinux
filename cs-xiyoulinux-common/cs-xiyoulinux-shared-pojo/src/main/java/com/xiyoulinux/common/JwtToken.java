package com.xiyoulinux.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 授权中心鉴权之后给客户端的 Token
 *
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "鉴权的token")
public class JwtToken implements Serializable {

    private static final long serialVersionUID = 2577537650024958206L;
    /**
     * JWT
     */
    @ApiModelProperty(value = "鉴权的字符串")
    private String token;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
}
