package com.xiyoulinux.join.controller;

import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.PassportException;
import com.xiyoulinux.exception.business.UserJoinException;
import com.xiyoulinux.join.pojo.bo.UserJoinBO;
import com.xiyoulinux.join.pojo.factory.InterviewStatus;
import com.xiyoulinux.join.pojo.factory.InterviewStatusFactory;
import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;
import com.xiyoulinux.join.pojo.vo.JoinSettingBO;
import com.xiyoulinux.join.service.UserJoinService;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinSetting;
import com.xiyoulinux.joinadmin.pojo.dto.JoinSettingDTO;
import com.xiyoulinux.joinadmin.pojo.dto.UserJoinDTO;
import com.xiyoulinux.joinadmin.service.SignUpService;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author CoderZk
 */
@Api(value = "纳新报名接口", tags = {"纳新报名展示的相关接口"})
@RestController
@RequestMapping("/join")
@Slf4j
public class JoinController {

    @DubboReference
    private UserService userService;

    @DubboReference
    private SignUpService signUpService;

    @ApiOperation(value = "用户报名纳新", notes = "用户报名纳新", httpMethod = "POST")
    @PostMapping("/signUp")
    public JSONResult signUp(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid UserJoinBO userJoinBO) throws UserJoinException, PassportException {

        String mobile = userJoinBO.getMobile();
        String verificationCode = userJoinBO.getVerificationCode();

        // 1. 查询该 uid 是否是真实用户
        CsUser csUser = userService.queryUserByUid(userId);
        if (csUser == null) {
            throw new UserJoinException(ReturnCode.ERROR.code, "该uid未找到对应用户");
        }

        // 2. 判断短信验证码是否正确 TODO: 等写完SMS短信通知服务后, 修改短信验证码
        if (verificationCode.equals("123456")) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "短信验证码错误");
        }

        // 3. 设置对应信息 RPC 调用 join_admin
        UserJoinDTO userJoinDTO = new UserJoinDTO();
        BeanUtils.copyProperties(userJoinBO, userJoinDTO);
        signUpService.createUserJoinInfo(userId, userJoinDTO);

        return JSONResult.ok();
    }

    @ApiOperation(value = "查看该登录用户的面试状态", notes = "查看该登录用户的面试状态", httpMethod = "GET")
    @GetMapping("/userInterviewStatus")
    public JSONResult signUp(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        JoinInfo joinInfo = signUpService.queryUserInterviewStatus(userId);

        Integer round = joinInfo.getRound();
        Integer status = joinInfo.getStatus();

        InterviewStatusVO interviewStatusVO = InterviewStatusVO.builder().round(round).status(status).build();

        InterviewStatus interviewStatus = InterviewStatusFactory.getInterviewStatus(interviewStatusVO);

        String interviewStatusResult = interviewStatus.getInterviewStatus();

        return JSONResult.ok(interviewStatusResult);
    }

    @ApiOperation(value = "获取纳新报名的开始截止时间和面试轮次", notes = "获取纳新报名的开始截止时间和面试轮次", httpMethod = "GET")
    @GetMapping("/signUpStartEndTime")
    public JSONResult signUpStartEndTime() {
        JoinSettingDTO joinSettingDTO = signUpService.querySignUpStartEndTime();

        return JSONResult.ok(joinSettingDTO);
    }

}
