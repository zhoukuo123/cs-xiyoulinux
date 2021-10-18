package com.xiyoulinux.joinadmin.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "join_record")
public class JoinRecord {
    /**
     * record id 主键
     */
    @Id
    private String rid;

    private String uid;

    /**
     * 评级
     */
    private Integer grade;

    /**
     * 轮次
     */
    private Integer round;

    /**
     * 面试人的uid
     */
    private String interviewer;

    /**
     * 评价时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 评价更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "basic_skill")
    private String basicSkill;

    @Column(name = "extra_skill")
    private String extraSkill;

    /**
     * 总体评价
     */
    private String overall;

    /**
     * 获取record id 主键
     *
     * @return rid - record id 主键
     */
    public String getRid() {
        return rid;
    }

    /**
     * 设置record id 主键
     *
     * @param rid record id 主键
     */
    public void setRid(String rid) {
        this.rid = rid;
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
     * 获取评级
     *
     * @return grade - 评级
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置评级
     *
     * @param grade 评级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
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
     * 获取面试人的uid
     *
     * @return interviewer - 面试人的uid
     */
    public String getInterviewer() {
        return interviewer;
    }

    /**
     * 设置面试人的uid
     *
     * @param interviewer 面试人的uid
     */
    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    /**
     * 获取评价时间
     *
     * @return created_time - 评价时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置评价时间
     *
     * @param createdTime 评价时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取评价更新时间
     *
     * @return updated_time - 评价更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置评价更新时间
     *
     * @param updatedTime 评价更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return basic_skill
     */
    public String getBasicSkill() {
        return basicSkill;
    }

    /**
     * @param basicSkill
     */
    public void setBasicSkill(String basicSkill) {
        this.basicSkill = basicSkill;
    }

    /**
     * @return extra_skill
     */
    public String getExtraSkill() {
        return extraSkill;
    }

    /**
     * @param extraSkill
     */
    public void setExtraSkill(String extraSkill) {
        this.extraSkill = extraSkill;
    }

    /**
     * 获取总体评价
     *
     * @return overall - 总体评价
     */
    public String getOverall() {
        return overall;
    }

    /**
     * 设置总体评价
     *
     * @param overall 总体评价
     */
    public void setOverall(String overall) {
        this.overall = overall;
    }
}