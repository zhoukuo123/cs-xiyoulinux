package com.xiyoulinux.auth.service;

import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.LoginUserInfo;
import com.xiyoulinux.common.UsernameAndPassword;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * @author qkm
 */
public interface IJwtService {
    /**
     * 生成JWT Token, 使用默认的超时时间
     *
     * @param loginUserInfo 存入token中的信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    List<Object> builderToken(LoginUserInfo loginUserInfo, long time) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * 登陆并返回 token
     *
     * @param usernameAndPassword 登陆和注册的用户信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    JwtToken loginAndGetToken(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * 注册用户并生成 Token 返回
     *
     * @param usernameAndPassword 登陆和注册的用户信息
     * @return jwt token
     * @throws Exception 转变私钥的异常
     */
    JwtToken registerUserAndGetToken(UsernameAndPassword usernameAndPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException;


    /**
     * 刷新token
     * @param  loginUserInfo
     */
    JwtToken refresh(LoginUserInfo loginUserInfo) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
