package com.xiyoulinux.joinadmin.service.impl;

import com.xiyoulinux.enums.InterviewStatus;
import com.xiyoulinux.joinadmin.mapper.*;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.JoinRecord;
import com.xiyoulinux.joinadmin.pojo.bo.InterviewEvaluationBO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewEvaluationRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoVO;
import com.xiyoulinux.joinadmin.service.InterviewService;
import com.xiyoulinux.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.n3r.idworker.Sid;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author CoderZk
 */
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private Sid sid;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private JoinQueueMapper joinQueueMapper;

    @Autowired
    private JoinInfoMapper joinInfoMapper;

    @Autowired
    private JoinQueueMapperCustom joinQueueMapperCustom;

    @Autowired
    private JoinRecordMapper joinRecordMapper;

    @Autowired
    private JoinRecordMapperCustom joinRecordMapperCustom;

    @DubboReference
    private UserService userService;


    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryIsSignUp(String sno) {
        JoinInfo joinInfo = queryJoinInfoBySno(sno);
        if (joinInfo == null) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryIsCheckIn(String sno) {
        JoinInfo joinInfo = queryJoinInfoBySno(sno);

        JoinQueue queryJoinQueue = new JoinQueue();
        queryJoinQueue.setUid(joinInfo.getUid());
        JoinQueue joinQueue = joinQueueMapper.selectOne(queryJoinQueue);

        if (joinQueue == null) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryIsInterviewedBySno(String sno) {
        JoinInfo joinInfo = queryJoinInfoBySno(sno);

        JoinQueue queryJoinQueue = new JoinQueue();
        queryJoinQueue.setUid(joinInfo.getUid());
        JoinQueue joinQueue = joinQueueMapper.selectOne(queryJoinQueue);
        Integer status = joinQueue.getStatus();

        if (status.equals(InterviewStatus.PENDING_DECISION.code)) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean startInterviewBySno(String sno, String interviewerUid) {

        JoinInfo joinInfo = queryJoinInfoBySno(sno);
        String uid = joinInfo.getUid();

        /**
         * 分布式锁 编写业务代码
         * 1. Redisson 是基于 Redis, 使用 Redisson 之前, 项目必须使用 Redis
         * 2. 注意 getLock 方法中的参数, 以 uid 作为参数, 每个 uid 一个 key, 和数据库中的行锁是一致的, 不会是方法级别的锁
         */
        RLock rLock = redissonClient.getLock("JOIN_QUEUE_UID_" + uid);

        if (rLock.tryLock()) {
            try {
                rLock.lock(5, TimeUnit.SECONDS);

                JoinQueue updateJoinQueue = new JoinQueue();
                updateJoinQueue.setStatus(InterviewStatus.PENDING_DECISION.code);
                updateJoinQueue.setInterviewer(interviewerUid);
                updateJoinQueue.setUpdatedTime(new Date());

                Example example = new Example(JoinQueue.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("uid", uid);

                joinQueueMapper.updateByExampleSelective(updateJoinQueue, example);
            } finally {
                /**
                 * 不管业务是否操作正确, 随后都要释放掉分布式锁
                 * 如果不释放, 过了超时时间也会自动释放
                 */
//                rLock.unlock();
            }
        } else {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public String getSnoByCheckInTime() {

        Map<String, Object> map = new HashMap<>();
        map.put("status", InterviewStatus.WAIT_INTERVIEW.code);

        return joinQueueMapperCustom.querySnoBySignInTime(map);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public IntervieweeInfoVO queryIntervieweeInfoBySno(String sno) {

        JoinInfo joinInfo = queryJoinInfoBySno(sno);

        IntervieweeInfoVO intervieweeInfoVO = new IntervieweeInfoVO();

        BeanUtils.copyProperties(joinInfo, intervieweeInfoVO);

        return intervieweeInfoVO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void addInterviewEvaluationRecord(String sno, InterviewEvaluationBO interviewEvaluationBO) {

        JoinInfo joinInfo = queryJoinInfoBySno(sno);

        JoinRecord joinRecord = new JoinRecord();

        String rid = sid.nextShort();
        joinRecord.setRid(rid);

        joinRecord.setUid(joinInfo.getUid());
        joinRecord.setBasicSkill(interviewEvaluationBO.getBasicSkill());
        joinRecord.setExtraSkill(interviewEvaluationBO.getExtraSkill());
        joinRecord.setOverall(interviewEvaluationBO.getOverall());
        joinRecord.setGrade(interviewEvaluationBO.getGrade());
        joinRecord.setRound(interviewEvaluationBO.getRound());
        joinRecord.setInterviewer(interviewEvaluationBO.getInterviewerUid());
        joinRecord.setCreatedTime(new Date());
        joinRecord.setUpdatedTime(new Date());

        joinRecordMapper.insert(joinRecord);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<InterviewRecordVO> queryPreviousInterviewRecords(String sno) {
        JoinInfo joinInfo = queryJoinInfoBySno(sno);

        Example joinRecordExp = new Example(JoinRecord.class);
        Example.Criteria criteria = joinRecordExp.createCriteria();
        criteria.andEqualTo("uid", joinInfo.getUid());

        List<JoinRecord> joinRecords = joinRecordMapper.selectByExample(joinRecordExp);
        List<InterviewRecordVO> interviewRecordVOList = new ArrayList<>();
        for (JoinRecord joinRecord : joinRecords) {

            InterviewRecordVO interviewRecordVO = InterviewRecordVO.builder()
                    .basicSkill(joinRecord.getBasicSkill()).extraSkill(joinRecord.getExtraSkill())
                    .overall(joinRecord.getOverall()).grade(joinRecord.getGrade())
                    .round(joinRecord.getRound()).build();

            String interviewerName = userService.queryUserNameByUid(joinRecord.getInterviewer());
            interviewRecordVO.setInterviewerName(interviewerName);

            interviewRecordVOList.add(interviewRecordVO);
        }

        return interviewRecordVOList;
    }

    @Override
    public List<InterviewEvaluationRecordVO> queryInterviewEvaluationRecords(String uid) {

        Map<String, Object> map = new HashMap<>();
        map.put("interviewer", uid);

        return joinRecordMapperCustom.queryInterviewEvaluationRecord(map);
    }

    private JoinInfo queryJoinInfoBySno(String sno) {
        JoinInfo queryJoinInfo = new JoinInfo();
        queryJoinInfo.setSno(sno);

        return joinInfoMapper.selectOne(queryJoinInfo);
    }
}
