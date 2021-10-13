package com.xiyoulinux.auth.service;

import com.xiyoulinux.auth.service.pojo.Account;
import com.xiyoulinux.auth.service.pojo.AuthResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author CoderZk
 */
public interface AuthService {
    /**
     * generate token
     * @param userId
     * @return
     */
    AuthResponse tokennize(String userId);

    @PostMapping("/verify")
    AuthResponse verify(@RequestBody Account account);

    /**
     * 刷新 token
     * @param refresh 传入refreshToken
     * @return
     */
    AuthResponse refresh(String refresh);

    AuthResponse delete(Account account);
}
