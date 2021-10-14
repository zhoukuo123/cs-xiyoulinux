package com.xiyoulinux.auth.controller;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.annotation.IgnoreResponseAdvice;
import com.xiyoulinux.auth.service.JwtService;
import com.xiyoulinux.vo.JwtToken;
import com.xiyoulinux.vo.UsernameAndPassword;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 对外暴露的授权服务接口
 *
 * @author qkm
 */
@Slf4j
@RestController
@RequestMapping("/authority")
@Api(tags = "登陆与注册")
public class AuthorityController {

    @Resource
    private JwtService jwtService;


    /**
     * 从授权中心获取 Token (登录功能), 且返回信息中没有统一响应的包装
     */
    @IgnoreResponseAdvice
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usernameAndPassword", paramType = "body")
    })
    @ApiResponse(code = 200,message = "返回token",response = JwtToken.class)
    @PostMapping("/token")
    public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword)
            throws Exception {

        log.info("request to get token with param: [{}]",
                JSON.toJSONString(usernameAndPassword));
        return jwtService.loginAndGetToken(usernameAndPassword);
    }

    /**
     * 注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户
     */
    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword)
            throws Exception {

        log.info("register user with param: [{}]", JSON.toJSONString(
                usernameAndPassword
        ));
        return jwtService.registerUserAndGetToken(
                usernameAndPassword
        );
    }
}
