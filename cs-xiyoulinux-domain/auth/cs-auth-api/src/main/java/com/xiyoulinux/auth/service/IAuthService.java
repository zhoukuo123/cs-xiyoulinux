package com.xiyoulinux.auth.service;

import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.LoginUserInfo;
import com.xiyoulinux.common.UsernameAndPassword;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author qkm
 */
public interface IAuthService {

    /**
     * 从授权中心获取 Token (登录功能), 且返回信息中没有统一响应的包装
     */
    JwtToken login(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException;


    /**
     * 注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户
     */
    JwtToken register(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * 刷新token
     */
    JwtToken refresh(LoginUserInfo loginUserInfo) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
