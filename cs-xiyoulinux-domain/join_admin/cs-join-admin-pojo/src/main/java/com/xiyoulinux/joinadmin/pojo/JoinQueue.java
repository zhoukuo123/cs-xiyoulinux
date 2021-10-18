package com.xiyoulinux.joinadmin.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "join_queue")
public class JoinQueue {
    @Id
    private String qid;

    private String uid;

    /**
     * 是否被面试 0: 未被面试(待面试队列) 2: 已被面试(已面试队列)
     */
    private Integer status;

    private String interviewer;

    /**
     * 签到时间
     */
    @Column(name = "signin_time")
    private Date signinTime;

    private Integer round;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return qid
     */
    public String getQid() {
        return qid;
    }

    /**
     * @param qid
     */
    public void setQid(String qid) {
        this.qid = qid;
    }

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
     * 获取是否被面试 0: 未被面试(待面试队列) 2: 已被面试(已面试队列)
     *
     * @return status - 是否被面试 0: 未被面试(待面试队列) 2: 已被面试(已面试队列)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否被面试 0: 未被面试(待面试队列) 2: 已被面试(已面试队列)
     *
     * @param status 是否被面试 0: 未被面试(待面试队列) 2: 已被面试(已面试队列)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return interviewer
     */
    public String getInterviewer() {
        return interviewer;
    }

    /**
     * @param interviewer
     */
    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    /**
     * 获取签到时间
     *
     * @return signin_time - 签到时间
     */
    public Date getSigninTime() {
        return signinTime;
    }

    /**
     * 设置签到时间
     *
     * @param signinTime 签到时间
     */
    public void setSigninTime(Date signinTime) {
        this.signinTime = signinTime;
    }

    /**
     * @return round
     */
    public Integer getRound() {
        return round;
    }

    /**
     * @param round
     */
    public void setRound(Integer round) {
        this.round = round;
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