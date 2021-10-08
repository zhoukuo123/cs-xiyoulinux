package com.xiyoulinux.user.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author CoderZk
 */
@Table(name = "cs_user")
public class CsUser implements Serializable {
    @Id
    private String uid;

    private String name;

    private String phone;

    /**
     * 默认为0, 1代表有权限查看纳新管理页面
     */
    private Integer privilege;

    private String password;

    /**
     * 1: 男 0: 女
     */
    private Integer sex;

    /**
     * 用户的个性签名
     */
    private String signature;

    /**
     * 头像位置
     */
    private String face;

    private String mail;

    private String qq;

    private String wechat;

    private String blog;

    private String github;

    private String grade;

    private String major;

    private String workplace;

    private String job;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取默认为0, 1代表有权限查看纳新管理页面
     *
     * @return privilege - 默认为0, 1代表有权限查看纳新管理页面
     */
    public Integer getPrivilege() {
        return privilege;
    }

    /**
     * 设置默认为0, 1代表有权限查看纳新管理页面
     *
     * @param privilege 默认为0, 1代表有权限查看纳新管理页面
     */
    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取1: 男 0: 女
     *
     * @return sex - 1: 男 0: 女
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置1: 男 0: 女
     *
     * @param sex 1: 男 0: 女
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取用户的个性签名
     *
     * @return signature - 用户的个性签名
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 设置用户的个性签名
     *
     * @param signature 用户的个性签名
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 获取头像位置
     *
     * @return face - 头像位置
     */
    public String getFace() {
        return face;
    }

    /**
     * 设置头像位置
     *
     * @param face 头像位置
     */
    public void setFace(String face) {
        this.face = face;
    }

    /**
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * @param qq
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * @return wechat
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * @param wechat
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * @return blog
     */
    public String getBlog() {
        return blog;
    }

    /**
     * @param blog
     */
    public void setBlog(String blog) {
        this.blog = blog;
    }

    /**
     * @return github
     */
    public String getGithub() {
        return github;
    }

    /**
     * @param github
     */
    public void setGithub(String github) {
        this.github = github;
    }

    /**
     * @return grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @param major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * @return workplace
     */
    public String getWorkplace() {
        return workplace;
    }

    /**
     * @param workplace
     */
    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    /**
     * @return job
     */
    public String getJob() {
        return job;
    }

    /**
     * @param job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}