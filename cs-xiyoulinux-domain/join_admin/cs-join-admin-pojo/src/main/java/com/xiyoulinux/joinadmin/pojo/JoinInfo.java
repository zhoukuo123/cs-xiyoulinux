package com.xiyoulinux.joinadmin.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "join_info")
public class JoinInfo {
    @Id
    private String uid;

    /**
     * 学号
     */
    private String sno;

    private String password;

    private String name;

    @Column(name = "class_name")
    private String className;

    private String mobile;

    /**
     * 轮次
     */
    private Integer round;

    /**
     * 面试状态, 1: 通过 -1: 淘汰 0: 等待面试 2: 面试完成, 待决策
     */
    private Integer status;

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
     * 获取学号
     *
     * @return sno - 学号
     */
    public String getSno() {
        return sno;
    }

    /**
     * 设置学号
     *
     * @param sno 学号
     */
    public void setSno(String sno) {
        this.sno = sno;
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
     * @return class_name
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取轮次
     *
     * @return round - 轮次
     */
    public Integer getRound() {
        return round;
    }

    /**
     * 设置轮次
     *
     * @param round 轮次
     */
    public void setRound(Integer round) {
        this.round = round;
    }

    /**
     * 获取面试状态, 1: 通过 -1: 淘汰 0: 等待面试 2: 面试完成, 待决策
     *
     * @return status - 面试状态, 1: 通过 -1: 淘汰 0: 等待面试 2: 面试完成, 待决策
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置面试状态, 1: 通过 -1: 淘汰 0: 等待面试 2: 面试完成, 待决策
     *
     * @param status 面试状态, 1: 通过 -1: 淘汰 0: 等待面试 2: 面试完成, 待决策
     */
    public void setStatus(Integer status) {
        this.status = status;
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