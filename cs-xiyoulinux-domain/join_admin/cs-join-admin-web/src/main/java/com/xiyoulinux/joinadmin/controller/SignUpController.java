package com.xiyoulinux.joinadmin.controller;

import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.InterviewException;
import com.xiyoulinux.exception.business.SignUpException;
import com.xiyoulinux.join.pojo.factory.InterviewStatus;
import com.xiyoulinux.join.pojo.factory.InterviewStatusFactory;
import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.joinadmin.pojo.vo.SignUpRecordVO;
import com.xiyoulinux.joinadmin.service.InterviewService;
import com.xiyoulinux.joinadmin.service.SignUpService;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.pojo.PagedGridResult;
import com.xiyoulinux.search.service.SignUpEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.xiyoulinux.controller.BaseController.COMMON_PAGE_SIZE;

/**
 * @author CoderZk
 */
@Api(value = "查看报名记录接口", tags = {"查看报名记录的相关接口"})
@RestController
@RequestMapping("/signUp")
@Slf4j
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private InterviewService interviewService;

    @DubboReference
    private SignUpEsService signUpEsService;

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "查看报名记录", notes = "查看报名记录", httpMethod = "GET")
    @GetMapping("/signUpRecord")
    public JSONResult signUpRecord(
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

        PagedGridResult grid = signUpService.querySignUpRecord(page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索报名记录", notes = "搜索报名记录", httpMethod = "GET")
    @GetMapping("/es/searchSignUpRecord")
    public JSONResult searchSignUpRecord(
            @RequestParam String keywords, @RequestParam String sort,
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
        PagedGridResult grid = signUpEsService.searchSignUpRecords(keywords, sort, page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "用户纳新签到", notes = "用户纳新签到", httpMethod = "POST")
    @PostMapping("/checkIn")
    public JSONResult checkIn(@RequestParam String sno) throws SignUpException {

        // 1. 查询该学号是否已报名纳新
        JoinInfo joinInfo = signUpService.queryJoinInfo(sno);

        if (joinInfo == null) {
            throw new SignUpException(ReturnCode.INVALID_PARAM.code, "该学号未报名纳新, 无法签到");
        }

        // 2. 判断该用户是否已经被面试, 本轮面试尚待决策
        if (joinInfo.getStatus().equals(com.xiyoulinux.enums.InterviewStatus.PENDING_DECISION.code)) {
            throw new SignUpException(ReturnCode.INVALID_PARAM.code, "该学号对应用户已被面试, 面试尚待决策, 无法签到");
        }

        // 3. 插入到db
        boolean result = signUpService.createUserToJoinQueue(joinInfo);
        if (!result) {
            throw new SignUpException(ReturnCode.INVALID_PARAM.code, "该学号对应用户已被淘汰, 无法签到");
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "查询待面试队列", notes = "查询待面试队列", httpMethod = "GET")
    @GetMapping("/pendingInterviewQueue")
    public JSONResult pendingInterviewQueue(
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

        PagedGridResult grid = signUpService.queryPendingInterviewQueue(page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "查询已面试队列", notes = "查询已面试队列", httpMethod = "GET")
    @GetMapping("/interviewedQueue")
    public JSONResult interviewedQueue(
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam(required = false) Integer pageSize) {

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = signUpService.queryInterviewedQueue(page, pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "删除签到记录", notes = "删除签到记录", httpMethod = "DELETE")
    @DeleteMapping("/checkInRecord")
    public JSONResult checkInRecord(@RequestParam String qid) throws SignUpException {

        boolean result = signUpService.deleteCheckInRecord(qid);
        if (!result) {
            throw new SignUpException(ReturnCode.INVALID_PARAM.code, "该记录编号不存在, 删除失败");
        }

        return JSONResult.ok();
    }


}
