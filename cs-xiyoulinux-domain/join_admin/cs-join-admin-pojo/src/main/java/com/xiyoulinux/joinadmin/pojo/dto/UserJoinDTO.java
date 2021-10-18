package com.xiyoulinux.joinadmin.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户报名纳新DTO", description = "DTO用于RPC调用传参")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDTO implements Serializable {

    @ApiModelProperty(value = "学号", name = "sno", example = "04192091", required = true)
    private String sno;

    @ApiModelProperty(value = "姓名", name = "name", example = "周阔", required = true)
    private String name;

    @ApiModelProperty(value = "班级", name = "className", example = "软件工程2101", required = true)
    private String className;

    @ApiModelProperty(value = "手机号", name = "mobile", example = "19832611520", required = true)
    private String mobile;

    @ApiModelProperty(value = "手机验证码", name = "verificationCode", example = "538355", required = true)
    private String verificationCode;
}
