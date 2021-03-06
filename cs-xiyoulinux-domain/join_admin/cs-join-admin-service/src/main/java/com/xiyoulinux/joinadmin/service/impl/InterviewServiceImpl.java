package com.xiyoulinux.joinadmin.service.impl;

import com.github.pagehelper.PageHelper;
import com.xiyoulinux.enums.InterviewStatus;
import com.xiyoulinux.joinadmin.mapper.*;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.JoinRecord;
import com.xiyoulinux.joinadmin.pojo.JoinSetting;
import com.xiyoulinux.joinadmin.pojo.bo.InterviewEvaluationBO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewEvaluationRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoAndGradeVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoVO;
import com.xiyoulinux.joinadmin.service.InterviewService;
import com.xiyoulinux.pojo.PagedGridResult;
import com.xiyoulinux.service.BaseService;
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
public class InterviewServiceImpl extends BaseService implements InterviewService {

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

    @Autowired
    private JoinSettingMapper joinSettingMapper;

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
         * ???????????? ??????????????????
         * 1. Redisson ????????? Redis, ?????? Redisson ??????, ?????????????????? Redis
         * 2. ?????? getLock ??????????????????, ??? uid ????????????, ?????? uid ?????? key, ????????????????????????????????????, ???????????????????????????
         */
        RLock rLock = redissonClient.getLock("JOIN_QUEUE_UID_" + uid);

        // ????????????, tryLock, ?????????, ?????????????????????, ??? return false
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
                 * ??????????????????????????????, ?????????????????????????????????
                 * ???????????????, ????????????????????????????????????
                 */
                rLock.unlock();
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

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryInterviewEvaluationRecords(String uid, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("interviewer", uid);

        PageHelper.startPage(page, pageSize);

        List<InterviewEvaluationRecordVO> interviewEvaluationRecordVOList = joinRecordMapperCustom.queryInterviewEvaluationRecord(map);

        return setterPagedGrid(interviewEvaluationRecordVOList, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<Integer> queryInterviewNumberStatistics() {

        Example joinInfoExp = new Example(JoinInfo.class);
        Example.Criteria criteria = joinInfoExp.createCriteria();
        criteria.andEqualTo("round", 1);

        int oneRoundCount = joinInfoMapper.selectCountByExample(joinInfoExp);

        Example joinInfoExp2 = new Example(JoinInfo.class);
        Example.Criteria criteria2 = joinInfoExp2.createCriteria();
        criteria2.andEqualTo("round", 2);

        int twoRoundCount = joinInfoMapper.selectCountByExample(joinInfoExp2);

        Example joinInfoExp3 = new Example(JoinInfo.class);
        Example.Criteria criteria3 = joinInfoExp3.createCriteria();
        criteria3.andEqualTo("round", 3);

        int threeRoundCount = joinInfoMapper.selectCountByExample(joinInfoExp3);

        List<Integer> list = new ArrayList<>();
        list.add(oneRoundCount);
        list.add(twoRoundCount);
        list.add(threeRoundCount);

        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryInterviewInfoAndIntervieweeInfo(Integer round, Integer status, Integer status2,
                                                                                Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("round", round);
        map.put("status", status);
        map.put("status2", status2);

        PageHelper.startPage(page, pageSize);

        List<IntervieweeInfoAndGradeVO> intervieweeInfoAndGradeVOList = joinRecordMapperCustom.queryInterviewInfoAndIntervieweeInfo(map);

        return setterPagedGrid(intervieweeInfoAndGradeVOList, page);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void makeDecision(String uid, Integer round, boolean pass) {

        // ?????? round ??? status ??? join_info ???
        // ?????? join_info ???, ????????????????????????????????????????????????
        JoinInfo updateJoinInfo = new JoinInfo();

        if (pass) {
            updateJoinInfo.setRound(round + 1);
            updateJoinInfo.setStatus(0);
        } else {
            updateJoinInfo.setStatus(-1);
        }

        Example example = new Example(JoinInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid", uid);
        criteria.andEqualTo("round", round);

        joinInfoMapper.updateByExampleSelective(updateJoinInfo, example);

        // ??? join_queue ??? ?????? ????????????????????????
        JoinQueue deleteJoinQueue = new JoinQueue();
        deleteJoinQueue.setUid(uid);

        // ??????????????????????????????, ???????????????????????????
        joinQueueMapper.delete(deleteJoinQueue);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<String> queryBatchDecisionUid(Integer round, Integer grade) {

        Example joinRecordExp = new Example(JoinRecord.class);
        Example.Criteria criteria = joinRecordExp.createCriteria();
        criteria.andEqualTo("round", round);
        criteria.andEqualTo("grade", grade);

        List<JoinRecord> joinRecords = joinRecordMapper.selectByExample(joinRecordExp);

        List<String> uids = new ArrayList<>();
        for (JoinRecord joinRecord : joinRecords) {
            uids.add(joinRecord.getUid());
        }
        return uids;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void setJoinStartEndTime(Date startTime, Date endTime) {
        JoinSetting joinSetting = new JoinSetting();

        String id = sid.nextShort();

        joinSetting.setId(id);
        joinSetting.setJoinStartTime(startTime);
        joinSetting.setJoinEndTime(endTime);
        joinSetting.setCreatedTime(new Date());
        joinSetting.setUpdatedTime(new Date());

        joinSettingMapper.insert(joinSetting);
    }

    private JoinInfo queryJoinInfoBySno(String sno) {
        JoinInfo queryJoinInfo = new JoinInfo();
        queryJoinInfo.setSno(sno);

        return joinInfoMapper.selectOne(queryJoinInfo);
    }
}
