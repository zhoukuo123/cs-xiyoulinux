package com.xiyoulinux.joinadmin.service.impl;

import com.github.pagehelper.PageHelper;
import com.xiyoulinux.enums.InterviewStatus;
import com.xiyoulinux.join.pojo.factory.InterviewStatusFactory;
import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;
import com.xiyoulinux.joinadmin.mapper.JoinInfoMapper;
import com.xiyoulinux.joinadmin.mapper.JoinQueueMapper;
import com.xiyoulinux.joinadmin.mapper.JoinQueueMapperCustom;
import com.xiyoulinux.joinadmin.mapper.JoinSettingMapper;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.JoinSetting;
import com.xiyoulinux.joinadmin.pojo.dto.JoinSettingDTO;
import com.xiyoulinux.joinadmin.pojo.dto.UserJoinDTO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.joinadmin.service.SignUpService;
import com.xiyoulinux.pojo.PagedGridResult;
import com.xiyoulinux.service.BaseService;
import com.xiyoulinux.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author CoderZk
 */
@DubboService
public class SignUpServiceImpl extends BaseService implements SignUpService {

    @Autowired
    private Sid sid;

    @Autowired
    private JoinInfoMapper joinInfoMapper;

    @Autowired
    private JoinSettingMapper joinSettingMapper;

    @Autowired
    private JoinQueueMapper joinQueueMapper;

    @Autowired
    private JoinQueueMapperCustom joinQueueMapperCustom;

    @DubboReference
    private UserService userService;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<JoinInfo> querySignUpRecord() {

        return joinInfoMapper.selectAll();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public JoinInfo createUserJoinInfo(String uid, UserJoinDTO userJoinDTO) {

        JoinInfo joinInfo = new JoinInfo();
        joinInfo.setUid(uid);
        joinInfo.setSno(userJoinDTO.getSno());
        joinInfo.setName(joinInfo.getName());
        joinInfo.setClassName(joinInfo.getClassName());
        joinInfo.setMobile(joinInfo.getMobile());
        joinInfo.setRound(1);
        joinInfo.setStatus(InterviewStatus.WAIT_INTERVIEW.code);
        joinInfo.setCreatedTime(new Date());
        joinInfo.setUpdatedTime(new Date());

        joinInfoMapper.insert(joinInfo);
        return joinInfo;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public JoinInfo queryUserInterviewStatus(String uid) {

        return joinInfoMapper.selectByPrimaryKey(uid);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public JoinSettingDTO querySignUpStartEndTime() {
        List<JoinSetting> joinSettings = joinSettingMapper.selectAll();

        JoinSetting joinSetting = joinSettings.get(0);
        JoinSettingDTO joinSettingDTO = new JoinSettingDTO();
        BeanUtils.copyProperties(joinSetting, joinSettingDTO);
        return joinSettingDTO;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public JoinInfo queryJoinInfo(String sno) {
        JoinInfo queryJoinInfo = new JoinInfo();
        queryJoinInfo.setSno(sno);
        return joinInfoMapper.selectOne(queryJoinInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean createUserToJoinQueue(JoinInfo joinInfo) {
        JoinQueue joinQueue = new JoinQueue();
        String qid = sid.nextShort();
        joinQueue.setQid(qid);
        joinQueue.setUid(joinInfo.getUid());
        joinQueue.setStatus(InterviewStatus.WAIT_INTERVIEW.code);
        joinQueue.setSigninTime(new Date());

        Integer round = joinInfo.getRound();
        Integer status = joinInfo.getStatus();

        if (status.equals(InterviewStatus.OUT.code)) {
            return false;
        }

        joinQueue.setRound(round);
        joinQueue.setCreatedTime(new Date());
        joinQueue.setUpdatedTime(new Date());

        joinQueueMapper.insert(joinQueue);
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryPendingInterviewQueue(Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("status", InterviewStatus.WAIT_INTERVIEW.code);

        PageHelper.startPage(page, pageSize);

        List<InterviewQueueVO> interviewQueueVOList = joinQueueMapperCustom.queryInterviewQueueMessage(map);

        return setterPagedGrid(interviewQueueVOList, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryInterviewedQueue(Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("status", InterviewStatus.PENDING_DECISION.code);

        PageHelper.startPage(page, pageSize);

        List<InterviewQueueVO> interviewQueueVOList = joinQueueMapperCustom.queryInterviewQueueMessage(map);

        for (InterviewQueueVO interviewQueueVO : interviewQueueVOList) {

            String interviewerName = userService.queryUserNameByUid(interviewQueueVO.getInterviewerName());
            interviewQueueVO.setInterviewerName(interviewerName);
        }

        return setterPagedGrid(interviewQueueVOList, page);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteCheckInRecord(String qid) {
        int result = joinQueueMapper.deleteByPrimaryKey(qid);
        return result == 0 ? false : true;
    }
}
