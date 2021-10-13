package com.xiyoulinux.auth.serivce.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.xiyoulinux.auth.service.pojo.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author CoderZk
 */
@Slf4j
@Service
public class JwtService {

    // 生产环境不能这么用
    private static final String KEY = "changIt";
    private static final String ISSUE = "zhoukuo";

    // 毫秒为单位, 这是1天
    public static final long TOKEN_EXP_TIME = 24 * 3600 * 1000;
    private static final String USER_ID = "userid";

    /**
     * 生成token
     * @param acct
     * @return
     */
    public String token(Account acct) {
        Date now = new Date();
        // TODO 修改 jwt token
        // 这里提供了很多种加密算法，生产环境可以用更高等级的加密算法，比如
        // 【最常用】采用非对称密钥加密，auth-service只负责生成jwt-token
        //  由各个业务方（或网关层）在自己的代码里用key校验token的正确性
        //  优点：符合规范，并且节约了一次 HTTP 远程调用, 减轻了网关层的压力, 使得 RT response time 变短
        //
        //  这里用了简单的token生成方式
        Algorithm algorithm = Algorithm.HMAC256(KEY);

        String token = JWT.create()
                .withIssuer(ISSUE)
                .withIssuedAt(now)
                // token 过期时间
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXP_TIME))
                .withClaim(USER_ID, acct.getUserId())
                .sign(algorithm);

        log.info("jwt generated, userId={}", acct.getUserId());
        return token;
    }

    /**
     * 校验token
     * @param token
     * @param userId
     * @return
     */
    public boolean verify(String token, String userId) {
        log.info("verifying jwt - username={}", userId);

        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUE)
                    .withClaim(USER_ID, userId)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("auth failed, userId={}", userId);
            return false;
        }
    }
}
