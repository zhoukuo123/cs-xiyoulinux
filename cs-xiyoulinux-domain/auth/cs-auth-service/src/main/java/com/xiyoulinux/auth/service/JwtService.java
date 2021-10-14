package com.xiyoulinux.auth.service;

import com.xiyoulinux.vo.JwtToken;
import com.xiyoulinux.vo.LoginUserInfo;
import com.xiyoulinux.vo.UsernameAndPassword;

/**
 * @author qkm
 */
public interface JwtService {
    /**
     * 生成JWT Token, 使用默认的超时时间
     *
     * @param loginUserInfo 存入token中的信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    String getToken(LoginUserInfo loginUserInfo) throws Exception;

    /**
     * 登陆并返回 token
     *
     * @param usernameAndPassword 登陆和注册的用户信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    JwtToken loginAndGetToken(UsernameAndPassword usernameAndPassword) throws Exception;

    /**
     * 注册用户并生成 Token 返回
     *
     * @param usernameAndPassword 登陆和注册的用户信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    JwtToken registerUserAndGetToken(UsernameAndPassword usernameAndPassword)
            throws Exception;

}
