package com.xiyoulinux.user.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户对象BO", description = "从网页, 由用户传入的数据封装在此entity中")
public class UserBO implements Serializable {

    @ApiModelProperty(value = "用户名", name = "username", example = "zk", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = true)
    private String confirmPassword;

    @ApiModelProperty(value = "手机号", name = "phone", example = "19832611520", required = true)
    private String phone;

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
