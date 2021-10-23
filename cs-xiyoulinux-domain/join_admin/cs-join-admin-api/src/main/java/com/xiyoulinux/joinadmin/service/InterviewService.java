package com.xiyoulinux.joinadmin.service;

import com.xiyoulinux.joinadmin.pojo.bo.InterviewEvaluationBO;
import com.xiyoulinux.joinadmin.pojo.vo.*;
import com.xiyoulinux.pojo.PagedGridResult;

import javax.print.attribute.standard.PagesPerMinute;
import java.util.Date;
import java.util.List;

/**
 * @author CoderZk
 */
public interface InterviewService {

    /**
     * 查询该学号的用户是否报名纳新
     *
     * @param sno 学号
     * @return true: 报名了 false: 未报名
     */
    boolean queryIsSignUp(String sno);

    /**
     * 查询该学号的用户是否已签到
     *
     * @param sno 学号
     * @return true: 签到了 false: 未签到
     */
    boolean queryIsCheckIn(String sno);

    /**
     * 查询该学号的用户是否已被面试
     *
     * @param sno 学号
     * @return true: 已经被面试 false: 未被面试
     */
    boolean queryIsInterviewedBySno(String sno);

    /**
     * 根据学号, 开始面试
     *
     * @param sno            学号
     * @param interviewerUid 面试人的uid
     * @return true: 已设置面试状态和面试人信息 false: 该学号对应的用户正在被面试
     */
    boolean startInterviewBySno(String sno, String interviewerUid);

    /**
     * 根据签到时间, 拿出最早签到的用户学号
     *
     * @return 学号
     */
    String getSnoByCheckInTime();

    /**
     * 根据学号, 查询被面试人的信息
     *
     * @return 被面试人的相关信息
     */
    IntervieweeInfoVO queryIntervieweeInfoBySno(String sno);

    /**
     * 增加面试评价记录
     */
    void addInterviewEvaluationRecord(String sno, InterviewEvaluationBO interviewEvaluationBO);

    /**
     * 查询之前的面试记录
     */
    List<InterviewRecordVO> queryPreviousInterviewRecords(String sno);

    /**
     * 查询当前登录用户的评价记录 分页
     *
     * @param uid 面试官uid
     * @return
     */
    PagedGridResult queryInterviewEvaluationRecords(String uid, Integer page, Integer pageSize);

    /**
     * 查询一面二面三面面试人数统计
     */
    List<Integer> queryInterviewNumberStatistics();

    /**
     * 查询面试信息和被面试人信息用于决策 分页
     */
    PagedGridResult queryInterviewInfoAndIntervieweeInfo(Integer round, Integer status, Integer status2,
                                                                         Integer page, Integer pageSize);

    /**
     * 决策 修改 round 和 status 在 join_info 和 join_queue 上
     */
    void makeDecision(String uid, Integer round, boolean pass);

    /**
     * 设置面试 开始时间和截止时间
     */
    void setJoinStartEndTime(Date startTime, Date endTime);
}
