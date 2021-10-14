package com.xiyoulinux.user.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户对象BO", description = "从网页, 由用户传入的数据封装在此entity中")
public class UserBO implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", example = "zk", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位")
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位")
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = true)
    private String confirmPassword;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", name = "phone", example = "19832611520", required = true)
    private String phone;

    @NotBlank(message = "手机验证码不能为空")
    @ApiModelProperty(value = "手机验证码", name = "verificationCode", example = "538355", required = true)
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
