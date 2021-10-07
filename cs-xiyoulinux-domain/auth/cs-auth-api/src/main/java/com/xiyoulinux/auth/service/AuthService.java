package com.xiyoulinux.auth.service;

import com.xiyoulinux.auth.service.pojo.Account;
import com.xiyoulinux.auth.service.pojo.AuthResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CoderZk
 */
public interface AuthService {
    /**
     * generate token
     * @param userId
     * @return
     */
    @PostMapping("/token")
    AuthResponse tokennize(@RequestParam("userId") String userId);

//    @PostMapping("/verify")
//    AuthResponse verify(@RequestBody Account account);

    /**
     * refresh token
     * @param refresh refreshToken
     * @return
     */
    @PostMapping("/refresh")
    AuthResponse refresh(@RequestParam("refresh") String refresh);

    @DeleteMapping("/delete")
    AuthResponse delete(@RequestBody Account account);
}
