package com.xiyoulinux.auth.intelImpl;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.service.IAuthService;
import com.xiyoulinux.auth.service.JwtService;
import com.xiyoulinux.common.JwtToken;
import com.xiyoulinux.common.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author qkm
 */
@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Autowired
   private JwtService jwtService;


    @Override
    public JwtToken login(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("request to get token with param: [{}]",
                JSON.toJSONString(usernameAndPassword));
        return jwtService.loginAndGetToken(usernameAndPassword);
    }

    @Override
    public JwtToken register(UsernameAndPassword usernameAndPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("register user with param: [{}]", JSON.toJSONString(
                usernameAndPassword
        ));
        return jwtService.registerUserAndGetToken(
                usernameAndPassword
        );
    }
}
