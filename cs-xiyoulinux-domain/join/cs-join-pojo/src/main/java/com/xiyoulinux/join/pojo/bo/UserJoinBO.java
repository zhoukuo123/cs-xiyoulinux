package com.xiyoulinux.join.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户报名纳新BO", description = "从网页, 由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinBO implements Serializable {

    @NotBlank(message = "学号不能为空")
    @Length(min = 8, max = 8, message = "学号必须为8位")
    @ApiModelProperty(value = "学号", name = "sno", example = "04192091", required = true)
    private String sno;

//    @NotBlank(message = "密码不能为空")
//    @Length(max = 32, message = "密码应小于32位")
//    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = false)
//    private String password;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", name = "name", example = "周阔", required = true)
    private String name;

    @NotBlank(message = "班级不能为空")
    @ApiModelProperty(value = "班级", name = "className", example = "软件工程2101", required = true)
    private String className;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", name = "mobile", example = "19832611520", required = true)
    private String mobile;

    @NotBlank(message = "手机验证码不能为空")
    @ApiModelProperty(value = "手机验证码", name = "verificationCode", example = "538355", required = true)
    private String verificationCode;
}
