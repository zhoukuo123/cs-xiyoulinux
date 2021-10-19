package com.xiyoulinux.joinadmin.controller;

import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.InterviewException;
import com.xiyoulinux.joinadmin.pojo.bo.InterviewEvaluationBO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewEvaluationRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoVO;
import com.xiyoulinux.joinadmin.service.InterviewService;
import com.xiyoulinux.pojo.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CoderZk
 */
@Api(value = "面试相关接口", tags = {"面试相关接口"})
@RestController
@RequestMapping("/interview")
@Slf4j
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @ApiOperation(value = "开始面试(根据输入的学号)", notes = "开始面试(根据输入的学号)", httpMethod = "POST")
    @PostMapping("/startInterviewBySno")
    public JSONResult startInterviewBySno(@RequestParam String sno, @RequestParam String interviewerUid) throws InterviewException {

        // 1. 查询该学号的用户是否报名纳新
        boolean isSignUp = interviewService.queryIsSignUp(sno);
        if (!isSignUp) {
            throw new InterviewException(ReturnCode.INVALID_PARAM.code, "该学号的用户尚未报名本次纳新");
        }

        // 2. 查询该学号的用户是否已签到
        boolean isCheckIn = interviewService.queryIsCheckIn(sno);
        if (!isCheckIn) {
            throw new InterviewException(ReturnCode.INVALID_PARAM.code, "该学号的用户尚未签到");
        }

        // 3. 查询该学号的用户是否已被面试
        boolean isInterviewed = interviewService.queryIsInterviewedBySno(sno);
        if (isInterviewed) {
            throw new InterviewException(ReturnCode.INVALID_PARAM.code, "该学号用户已被面试");
        }

        // 4. 开始面试, 添加面试人, 改变面试状态
        boolean result = interviewService.startInterviewBySno(sno, interviewerUid);
        if (!result) {
            throw new InterviewException(ReturnCode.INVALID_PARAM.code, "该学号用户正在被面试, 请重新获取学号");
        }

        return JSONResult.ok(sno);
    }

    @ApiOperation(value = "自动叫号", notes = "自动叫号", httpMethod = "GET")
    @GetMapping("/automaticCall")
    public JSONResult automaticCall() {

        String sno = interviewService.getSnoByCheckInTime();

        return JSONResult.ok(sno);
    }

    @ApiOperation(value = "查询被面试人信息", notes = "查询被面试人信息", httpMethod = "GET")
    @GetMapping("/intervieweeInfo")
    public JSONResult intervieweeInfo(@RequestParam String sno) {

        IntervieweeInfoVO intervieweeInfoVO = interviewService.queryIntervieweeInfoBySno(sno);

        return JSONResult.ok(intervieweeInfoVO);
    }

    @ApiOperation(value = "增加面试评价记录", notes = "增加面试评价记录", httpMethod = "POST")
    @PostMapping("/interviewRecord")
    public JSONResult interviewRecord(@RequestParam String sno, @RequestBody InterviewEvaluationBO interviewEvaluationBO) {

        interviewService.addInterviewEvaluationRecord(sno, interviewEvaluationBO);

        return JSONResult.ok();
    }

    @ApiOperation(value = "查询之前的面试记录", notes = "查询之前的面试记录", httpMethod = "GET")
    @GetMapping("/previousInterviewRecords")
    public JSONResult previousInterviewRecords(@RequestParam String sno) {

        List<InterviewRecordVO> interviewRecordVOList = interviewService.queryPreviousInterviewRecords(sno);

        return JSONResult.ok(interviewRecordVOList);
    }

    @ApiOperation(value = "查询我(当前登录用户)的评价记录", notes = "查询我的评价记录", httpMethod = "GET")
    @GetMapping("/interviewEvaluationRecords")
    public JSONResult interviewEvaluationRecords(@RequestParam String uid) {

        List<InterviewEvaluationRecordVO> interviewEvaluationRecordVOList = interviewService.queryInterviewEvaluationRecords(uid);

        return JSONResult.ok(interviewEvaluationRecordVOList);
    }








}
