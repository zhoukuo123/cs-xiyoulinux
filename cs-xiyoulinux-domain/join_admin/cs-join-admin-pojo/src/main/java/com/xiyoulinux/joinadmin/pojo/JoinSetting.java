package com.xiyoulinux.joinadmin.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "join_setting")
public class JoinSetting {
    @Id
    private String id;

    /**
     * 纳新报名开始时间
     */
    @Column(name = "join_start_time")
    private Date joinStartTime;

    /**
     * 纳新报名截止时间
     */
    @Column(name = "join_end_time")
    private Date joinEndTime;

    /**
     * 本次纳新有多少轮
     */
    private Integer round;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取纳新报名开始时间
     *
     * @return join_start_time - 纳新报名开始时间
     */
    public Date getJoinStartTime() {
        return joinStartTime;
    }

    /**
     * 设置纳新报名开始时间
     *
     * @param joinStartTime 纳新报名开始时间
     */
    public void setJoinStartTime(Date joinStartTime) {
        this.joinStartTime = joinStartTime;
    }

    /**
     * 获取纳新报名截止时间
     *
     * @return join_end_time - 纳新报名截止时间
     */
    public Date getJoinEndTime() {
        return joinEndTime;
    }

    /**
     * 设置纳新报名截止时间
     *
     * @param joinEndTime 纳新报名截止时间
     */
    public void setJoinEndTime(Date joinEndTime) {
        this.joinEndTime = joinEndTime;
    }

    /**
     * 获取本次纳新有多少轮
     *
     * @return round - 本次纳新有多少轮
     */
    public Integer getRound() {
        return round;
    }

    /**
     * 设置本次纳新有多少轮
     *
     * @param round 本次纳新有多少轮
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