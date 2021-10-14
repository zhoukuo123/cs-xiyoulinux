package com.xiyoulinux.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.service.JwtService;
import com.xiyoulinux.vo.JwtToken;
import com.xiyoulinux.vo.LoginUserInfo;
import com.xiyoulinux.vo.UsernameAndPassword;
import com.xiyoulinux.auth.vo.contstant.AuthorityConstant;
import com.xiyoulinux.constant.CommonConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author qkm
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {


    @Override
    public String getToken(LoginUserInfo loginUserInfo)
            throws Exception {

        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now().plus(AuthorityConstant.DEFAULT_EXPIRE_DAY, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());


        return Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public JwtToken loginAndGetToken(UsernameAndPassword usernameAndPassword) throws Exception {

        //调用用户中心验证用户，同时返回用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
        return new JwtToken(getToken(loginUserInfo), userId);
    }

    @Override
    public JwtToken registerUserAndGetToken(UsernameAndPassword usernameAndPassword)
            throws Exception {

        //调用用户中心注册用户，返回注册的用户id
        String userId = "1";
        LoginUserInfo loginUserInfo = new LoginUserInfo(userId, usernameAndPassword.getUsername());
        return new JwtToken(getToken(loginUserInfo), userId);

    }

    /**
     * 根据本地存储的私钥获取到 PrivateKey 对象
     */
    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec priPICS8 = new PKCS8EncodedKeySpec(
                Base64.getDecoder().decode(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPICS8);
    }
}
