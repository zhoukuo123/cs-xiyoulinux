package com.xiyoulinux.user.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户对象BO", description = "从网页, 由用户传入的数据封装在此entity中")
public class CenterUserBO {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 12, message = "用户名不能超过12位")
    @ApiModelProperty(value = "用户名", name = "username", example = "周阔", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = false)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = false)
    private String confirmPassword;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", name = "phone", example = "13999999999", required = true)
    private String phone;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value = "性别", name = "sex", example = "0:女 1:男 2:保密", required = true)
    private Integer sex;

    @Length(max = 64, message = "个性签名不能超过64位")
    @ApiModelProperty(value = "个性签名", name = "signature", example = "这个人很懒, 什么都没有留下.", required = false)
    private String signature;

    @Email
    @ApiModelProperty(value = "邮箱地址", name = "mail", example = "123123@qq.com", required = false)
    private String mail;

    @Length(min = 5, max = 12, message = "qq号不能超过12位")
    @ApiModelProperty(value = "qq号", name = "qq", example = "1178053011", required = false)
    private String qq;

    @Length(max = 32, message = "微信号不能超过32位")
    @ApiModelProperty(value = "微信号", name = "wechat", example = "wx123123", required = false)
    private String wechat;

    @ApiModelProperty(value = "博客地址", name = "blog", example = "https://blog.csdn.net/weixin_45626511", required = false)
    private String blog;

    @ApiModelProperty(value = "github地址", name = "github", example = "https://github.com/xxxxx", required = false)
    private String github;

    @Length(min = 4, max = 4, message = "届别必须为4位, 例如2018")
    @ApiModelProperty(value = "届别", name = "grade", example = "2019", required = false)
    private String grade;

    @Length(max = 32, message = "专业应小于32个字")
    @ApiModelProperty(value = "专业", name = "major", example = "计算机科学与技术", required = false)
    private String major;

    @ApiModelProperty(value = "工作地点", name = "workplace", example = "北京", required = false)
    private String workplace;

    @Length(max = 32, message = "职位应小于32个字")
    @ApiModelProperty(value = "职位", name = "job", example = "数据挖掘", required = false)
    private String job;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
