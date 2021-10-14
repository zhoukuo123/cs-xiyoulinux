package com.xiyoulinux.user.controller;

import com.xiyoulinux.auth.service.AuthService;
import com.xiyoulinux.auth.service.pojo.Account;
import com.xiyoulinux.auth.service.pojo.AuthCode;
import com.xiyoulinux.auth.service.pojo.AuthResponse;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.PassportException;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.UserBO;
import com.xiyoulinux.user.service.UserService;
import com.xiyoulinux.utils.CookieUtils;
import com.xiyoulinux.utils.JsonUtils;
import com.xiyoulinux.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * @author CoderZk
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
@Slf4j
public class PassportController {

    @Autowired
    private UserService userService;

    @DubboReference(loadbalance = "roundrobin")
    private AuthService authService;

    private static final String AUTH_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "refresh-token";
    private static final String UID_HEADER = "user-id";


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public JSONResult register(@RequestBody @Valid UserBO userBO) throws PassportException {
        // 校验
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        String phone = userBO.getPhone();
        String verificationCode = userBO.getVerificationCode();

        // 1. 查询手机号是否已经注册
        boolean isExist = userService.queryPhoneIsExist(phone);
        if (isExist) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "该手机号已经注册");
        }

        // 2. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "两次密码输入不一致");
        }

        // 3. 判断短信验证码是否正确 TODO: 等写完SMS短信通知服务后, 修改短信验证码
        if (verificationCode.equals("123456")) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "短信验证码错误");
        }

        // 4. 实现注册
        userService.createUser(userBO);

        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody @Valid UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String phone = userBO.getPhone();
        String password = userBO.getPassword();

        // 1. 实现登录
        CsUser userResult = userService.queryUserForLogin(phone, MD5Utils.getMD5Str(password));

        if (userResult == null) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "手机号或密码不正确");
        }

        // 调用Auth微服务生成token
        AuthResponse token = authService.tokennize(userResult.getUid());
        if (!AuthCode.SUCCESS.getCode().equals(token.getCode())) {
            log.error("Token error - uid={}", userResult.getUid());
            throw new PassportException(ReturnCode.UNAUTHORIZED.code, "Token error");
        }

        // 将token添加到header当中
        addAuth2Header(response, token.getAccount());

        // 把用户信息放入cookie中, 前端获取后显示为登录时的状态, 并且刷新也不会丢失登录状态
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        return JSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws PassportException {

        Account account = Account.builder()
                .token(request.getHeader(AUTH_HEADER))
                .refreshToken(request.getHeader(REFRESH_TOKEN_HEADER))
                .userId(userId)
                .build();
        AuthResponse auth = authService.delete(account);
        if (!AuthCode.SUCCESS.getCode().equals(auth.getCode())) {
            log.error("Token error - uid={}", userId);
            throw new PassportException(ReturnCode.UNAUTHORIZED.code, "Token error");
        }

        // 清除用户相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        return JSONResult.ok();
    }


    @ApiOperation(value = "找回密码", notes = "找回密码", httpMethod = "POST")
    @PostMapping("/retrievePassword")
    public JSONResult retrievePassword(@RequestBody UserBO userBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws PassportException {
        // 校验
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        String phone = userBO.getPhone();
        String verificationCode = userBO.getVerificationCode();


        // 1. 查询手机号是否已经注册
        boolean isExist = userService.queryPhoneIsExist(phone);
        if (!isExist) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "该手机号还未注册, 请先注册");
        }

        // 2. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "两次密码输入不一致");
        }

        // 3. 判断短信验证码是否正确 TODO: 等写完SMS短信通知服务后, 修改短信验证码
        if (verificationCode.equals("123456")) {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "短信验证码错误");
        }

        // 4. 修改密码
        userService.retrievePassword(userBO);

        return JSONResult.ok();
    }


    /**
     * 修改前端js代码, 在前端页面里拿到Authorization, refresh-token和user-id
     * 前端每次请求服务, 都把这几个参数带上
     *
     * @param response
     * @param token
     */
    private void addAuth2Header(HttpServletResponse response, Account token) {
        response.setHeader(AUTH_HEADER, token.getToken());
        response.setHeader(REFRESH_TOKEN_HEADER, token.getRefreshToken());
        response.setHeader(UID_HEADER, token.getUserId());

        // 让前端感知到, 过期时间一天, 这样可以在临近过期的时候refresh token
        Calendar expTime = Calendar.getInstance();
        expTime.add(Calendar.DAY_OF_MONTH, 1);
        response.setHeader("token-exp-time", expTime.getTimeInMillis() + "");
    }
}
