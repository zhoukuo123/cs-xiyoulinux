package com.xiyoulinux.joinadmin.service;

import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.dto.JoinSettingDTO;
import com.xiyoulinux.joinadmin.pojo.dto.UserJoinDTO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.pojo.PagedGridResult;

import java.util.List;

/**
 * @author CoderZk
 */
public interface SignUpService {
    /**
     * 查询报名记录
     */
    List<JoinInfo> querySignUpRecord();

    /**
     * 创建用户纳新信息
     */
    JoinInfo createUserJoinInfo(String uid , UserJoinDTO userJoinDTO);

    /**
     * 查询用户面试状态
     */
    JoinInfo queryUserInterviewStatus(String uid);

    /**
     * 查询开始纳新报名的时间
     */
    JoinSettingDTO querySignUpStartEndTime();

    /**
     * 根据学号查询该用户是否报名纳新
     */
    JoinInfo queryJoinInfo(String sno);

    /**
     * 加入一条记录到join_queue
     * @param joinInfo 用户报名信息
     * @return true: 签到成功 false: 签到失败, 该用户已被淘汰
     */
    boolean createUserToJoinQueue(JoinInfo joinInfo);

    /**
     * 查询待面试队列 分页
     */
    PagedGridResult queryPendingInterviewQueue(Integer page, Integer pageSize);

    /**
     * 查询已面试队列 分页
     */
    PagedGridResult queryInterviewedQueue(Integer page, Integer pageSize);

    /**
     * 删除签到记录
     */
    boolean deleteCheckInRecord(String qid);
}
