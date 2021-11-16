package com.xiyoulinux.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.constant.AuthorityConstant;
import com.xiyoulinux.auth.service.IJwtService;
import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.LoginUserInfo;
import com.xiyoulinux.common.TokenParseUtil;
import com.xiyoulinux.common.UsernameAndPassword;
import com.xiyoulinux.constant.AuthCommonConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;


/**
 * @author qkm
 */
@Slf4j
@Service
public class JwtServiceImpl implements IJwtService {

    @Override
    public List<Object> builderToken(LoginUserInfo loginUserInfo, long time) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + time);
        List<Object> list = new ArrayList<>();
        String token = Jwts.builder()
                // jwt payload --> KV
                .claim(AuthCommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expiredTime)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
        list.add(token);
        list.add(expiredTime);
        return list;

    }

    @Override
    public JwtToken loginAndGetToken(UsernameAndPassword usernameAndPassword) {

        //调用用户中心验证用户，同时返回用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
        try {
            return getJwtToken(loginUserInfo);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("get token error :[{}]", e.getMessage());
        }
        return null;
    }

    @Override
    public JwtToken registerUserAndGetToken(UsernameAndPassword usernameAndPassword) {
        //调用用户中心注册用户，返回注册的用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
        try {
            return getJwtToken(loginUserInfo);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("get token error :[{}]", e.getMessage());
        }
        return null;
    }

    @Override
    public JwtToken refresh(String token) {
        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse user info from token error: [{}]", ex.getMessage(), ex);
            return null;
        }
        try {
            return getJwtToken(loginUserInfo);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("get token error :[{}]",e.getMessage());
        }
        //表示获取token出问题
        return new JwtToken(null,token,null);
    }

    private JwtToken getJwtToken(LoginUserInfo loginUserInfo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = (String) builderToken(loginUserInfo, AuthorityConstant.DEFAULT_EXPIRE_DAY).get(0);
        List<Object> objects = builderToken(loginUserInfo, AuthorityConstant.DEFAULT_REFRESH_EXPIRE_DAY);
        String refreshToken = (String) objects.get(0);
        Date expiredTime = (Date) objects.get(1);
        return new JwtToken(token, refreshToken, expiredTime);
    }


    /**
     * 根据本地存储的私钥获取到 PrivateKey 对象
     */
    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec priPICS8 = new PKCS8EncodedKeySpec(
                Base64.getDecoder().decode(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPICS8);
    }

}
