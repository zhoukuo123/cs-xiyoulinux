package com.xiyoulinux.auth.controller;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.service.IJwtService;
import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.UsernameAndPassword;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author qkm
 */
@RestController
@RequestMapping("/authority")
@Slf4j
public class AuthorityController {
    private final IJwtService iJwtService;

    public AuthorityController(IJwtService iJwtService) {
        this.iJwtService = iJwtService;
    }

    @PostMapping("/login")
    public JSONResult login(@RequestBody UsernameAndPassword usernameAndPassword) {
        log.info("request to get token with param: [{}]",
                JSON.toJSONString(usernameAndPassword));
        JwtToken jwtToken = iJwtService.loginAndGetToken(usernameAndPassword);
        if (jwtToken == null) {
            return JSONResult.errorMsg(ReturnCode.ERROR.code, "get token error");
        }
        return JSONResult.ok(jwtToken);
    }

    @PostMapping("/register")
    public JSONResult register(@RequestBody UsernameAndPassword usernameAndPassword) {
        log.info("register user with param: [{}]", JSON.toJSONString(usernameAndPassword));
        JwtToken jwtToken = iJwtService.registerUserAndGetToken(usernameAndPassword);
        if (jwtToken == null) {
            return JSONResult.errorMsg(ReturnCode.ERROR.code, "get token error");
        }
        return JSONResult.ok(jwtToken);
    }

    @PostMapping("/refresh")
    public JSONResult refresh(HttpServletRequest request) {
        JwtToken refresh = iJwtService.refresh(request.getHeader("cs-user"));
        if (refresh == null) {
            return JSONResult.errorMsg(ReturnCode.UNAUTHORIZED.code, "not has auth!!!");
        }
        if (refresh.getToken() == null && refresh.getRefreshToken() != null) {
            return JSONResult.errorMsg(ReturnCode.ERROR.code, "get token error");
        }
        return JSONResult.ok(refresh);
    }

    public static void main(String[] args) {
        String  dateStr = "Tue Mar 01 22:06:19 CST 2022";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = new Date();
        try{
            date = sdf.parse(dateStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.err.println(formatStr);
    }
}
