package com.xiyoulinux.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.constant.AuthorityConstant;
import com.xiyoulinux.auth.service.IJwtService;
import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.LoginUserInfo;
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
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


/**
 * @author qkm
 */
@Slf4j
@Service
public class JwtServiceImpl implements IJwtService {

    @Override
    public String getToken(LoginUserInfo loginUserInfo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Date now = new Date();
        return Jwts.builder()
                // jwt payload --> KV
                .claim(AuthCommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(new Date(now.getTime() + AuthorityConstant.DEFAULT_EXPIRE_DAY))
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public JwtToken loginAndGetToken(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //调用用户中心验证用户，同时返回用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
//        return new JwtToken(getToken(loginUserInfo),userId);
        return new JwtToken(getToken(loginUserInfo),userId);
    }

    @Override
    public JwtToken registerUserAndGetToken(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //调用用户中心注册用户，返回注册的用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
//        return new JwtToken(getToken(loginUserInfo),userId);
        return new JwtToken(getToken(loginUserInfo),userId);

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
