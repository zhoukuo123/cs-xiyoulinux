package com.xiyoulinux.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author CoderZk
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = "com.xiyoulinux.auth.service.impl")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
