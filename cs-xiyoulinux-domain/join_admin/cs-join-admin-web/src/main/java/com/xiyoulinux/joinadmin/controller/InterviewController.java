package com.xiyoulinux.joinadmin.controller;

import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.InterviewException;
import com.xiyoulinux.exception.business.SignUpException;
import com.xiyoulinux.joinadmin.pojo.bo.InterviewEvaluationBO;
import com.xiyoulinux.joinadmin.pojo.dto.BatchDecisionDTO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoVO;
import com.xiyoulinux.joinadmin.service.InterviewService;
import com.xiyoulinux.joinadmin.stream.BatchDecisionTopic;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.pojo.PagedGridResult;
import com.xiyoulinux.search.service.SignUpEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.xiyoulinux.controller.BaseController.COMMON_PAGE_SIZE;

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

    @Autowired
    private SignUpEsService signUpEsService;

    @Autowired
    private BatchDecisionTopic producer;

    @ApiOperation(value = "开始面试(根据输入的学号), 修改join_queue, 是否开始面试是通过join_queue来判断的", notes = "开始面试(根据输入的学号)", httpMethod = "POST")
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
    public JSONResult interviewEvaluationRecords(@RequestParam String uid,
                                                 @ApiParam(name = "page", value = "第几页", required = false)
                                                 @RequestParam(required = false) Integer page,
                                                 @ApiParam(name = "pageSize", value = "每一页显示的条数", required = false)
                                                 @RequestParam(required = false) Integer pageSize) {

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = interviewService.queryInterviewEvaluationRecords(uid, page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "查询一面二面三面人数统计", notes = "查询一面二面三面人数统计", httpMethod = "GET")
    @GetMapping("/interviewNumberStatistics")
    public JSONResult interviewNumberStatistics() {

        List<Integer> statistics = interviewService.queryInterviewNumberStatistics();

        return JSONResult.ok(statistics);
    }

    @ApiOperation(value = "查询被面试人员信息和面试评级(用于决策)", notes = "查询被面试人员信息和面试评级(用于决策)", httpMethod = "GET")
    @GetMapping("/interviewInfo")
    public JSONResult interviewInfo(@RequestParam Integer round,
                                    @RequestParam Integer status,
                                    @ApiParam(name = "status2", value = "备用字段, 用于查询待定用户(包括未面试和已面试待决策), 在查询淘汰结果时, 不用传", required = false)
                                    @RequestParam Integer status2,

                                    @ApiParam(name = "page", value = "第几页", required = false)
                                    @RequestParam(required = false) Integer page,
                                    @ApiParam(name = "pageSize", value = "每一页显示的条数", required = false)
                                    @RequestParam(required = false) Integer pageSize) {

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = interviewService.queryInterviewInfoAndIntervieweeInfo(round, status, status2, page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "手动决策", notes = "手动决策", httpMethod = "POST")
    @PostMapping("/makeDecision")
    public JSONResult makeDecision(@RequestParam String uid,
                                   @RequestParam Integer round,
                                   @RequestParam boolean pass) {

        interviewService.makeDecision(uid, round, pass);

        return JSONResult.ok();
    }

    @ApiOperation(value = "批量通过, 批量淘汰", notes = "批量通过, 批量淘汰", httpMethod = "POST")
    @PostMapping("/batchDecision")
    public JSONResult batchDecision(@RequestParam Integer round,
                                    @RequestParam Integer grade,
                                    @RequestParam boolean pass) {

        List<String> uids = interviewService.queryBatchDecisionUid(round, grade);

        if (uids != null) {
            log.info("send batch decision message, decision is = {}", pass);

            BatchDecisionDTO batchDecisionDTO = BatchDecisionDTO.builder().uids(uids).pass(pass).round(round).build();

            producer.output().send(MessageBuilder.withPayload(batchDecisionDTO).build());
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "在面试结果中搜索某个学生", notes = "在面试结果中搜索某个学生", httpMethod = "POST")
    @PostMapping("/es/searchFromInterviewResult")
    public JSONResult searchFromInterviewResult(@RequestParam String keywords,
                                                @RequestParam Integer round,
                                                @RequestParam Integer status,
                                                @ApiParam(name = "status2", value = "备用字段, 用于查询待定用户(包括未面试和已面试待决策), 在查询淘汰结果时, 不用传", required = false)
                                                @RequestParam Integer status2,
                                                @ApiParam(name = "page", value = "第几页", required = false)
                                                @RequestParam(required = false) Integer page,
                                                @ApiParam(name = "pageSize", value = "每一页显示的条数", required = false)
                                                @RequestParam(required = false) Integer pageSize) throws SignUpException {

        if (StringUtils.isBlank(keywords)) {
            throw new SignUpException(ReturnCode.INVALID_PARAM.code, "搜索内容为空, 请重新输入");
        }

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // RPC 调用 cs-search
        PagedGridResult grid = signUpEsService.searchFromInterviewResult(keywords, round, status, status2, page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "设置面试开始时间和截止时间", notes = "设置面试开始时间和截止时间", httpMethod = "POST")
    @PostMapping("/setJoinStartEndTime")
    public JSONResult setJoinStartEndTime(@RequestParam Date startTime, @RequestParam Date endTime) {

        interviewService.setJoinStartEndTime(startTime, endTime);

        return JSONResult.ok();
    }


}
