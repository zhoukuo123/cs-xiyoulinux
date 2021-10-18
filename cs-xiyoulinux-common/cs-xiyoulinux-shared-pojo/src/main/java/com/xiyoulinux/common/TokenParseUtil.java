package com.xiyoulinux.common;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.constant.AuthCommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;

/**
 * JWT Token 解析工具类
 * @author qkm
 */
public class TokenParseUtil {

    /**
     * 从 JWT Token 中解析 LoginUserInfo 对象
     */
    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception {

        if (null == token) {
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();

        // 如果 Token 已经过期了, 返回 null
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

        // 返回 Token 中保存的用户信息
        return JSON.parseObject(
                body.get(AuthCommonConstant.JWT_USER_INFO_KEY).toString(),
                LoginUserInfo.class
        );
    }

    /**
     * 通过公钥去解析 JWT Token
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 根据本地存储的公钥获取到 PublicKey 对象
     */
    private static PublicKey getPublicKey() throws Exception {

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64.getDecoder().decode(AuthCommonConstant.PUBLIC_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

}
