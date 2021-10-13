package com.xiyoulinux.auth.serivce.impl;

import com.xiyoulinux.auth.service.AuthService;
import com.xiyoulinux.auth.service.pojo.Account;
import com.xiyoulinux.auth.service.pojo.AuthCode;
import com.xiyoulinux.auth.service.pojo.AuthResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author CoderZk
 */
@DubboService
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String USER_TOKEN = "USER_TOKEN-";

    @Override
    public AuthResponse tokennize(String userId) {
        Account account = Account.builder()
                .userId(userId)
                .build();

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(USER_TOKEN + userId, account, JwtService.TOKEN_EXP_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(account.getRefreshToken(), userId, JwtService.TOKEN_EXP_TIME, TimeUnit.MILLISECONDS);
        return AuthResponse.builder()
                .account(account)
                .code(AuthCode.SUCCESS.getCode())
                .build();
    }

    @Override
    public AuthResponse verify(Account account) {
        // 检查redis中当前token是否还生效
        Account redisAccount = (Account) redisTemplate.opsForValue().get(USER_TOKEN + account.getUserId());
        if (redisAccount == null) {
            return AuthResponse.builder()
                    .code(AuthCode.TOKEN_EXPIRED.getCode())
                    .build();
        }
        boolean success = jwtService.verify(account.getToken(), account.getUserId());
        return AuthResponse.builder()
                .code(success ? AuthCode.SUCCESS.getCode() : AuthCode.USER_NOT_FOUND.getCode())
                .build();
    }

    /**
     * TODO 有很多种方法实现自动刷新，比如前端主动调用（可以在AuthResponse里将过期时间返回给前端）
     */
    @Override
    public AuthResponse refresh(String refreshToken) {
        String userId = (String) redisTemplate.opsForValue().get(refreshToken);
        if (StringUtils.isBlank(userId)) {
            return AuthResponse.builder()
                    .code(AuthCode.USER_NOT_FOUND.getCode())
                    .build();
        }
        redisTemplate.delete(refreshToken);
        return tokennize(userId);
    }

    @Override
    public AuthResponse delete(Account account) {
        AuthResponse resp = new AuthResponse();
        if (account.isSkipVerification()) {
            redisTemplate.delete(USER_TOKEN + account.getUserId());
            resp.setCode(AuthCode.SUCCESS.getCode());
        } else {
            AuthResponse token = verify(account);

            if (AuthCode.SUCCESS.getCode().equals(token.getCode())) {
                redisTemplate.delete(USER_TOKEN + account.getUserId());
                redisTemplate.delete(account.getRefreshToken());
                resp.setCode(AuthCode.SUCCESS.getCode());
            } else {
                resp.setCode(AuthCode.USER_NOT_FOUND.getCode());
            }
        }
        return resp;
    }
}
