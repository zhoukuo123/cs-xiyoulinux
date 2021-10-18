package com.xiyoulinux.joinadmin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author CoderZk
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiyoulinux.joinadmin.mapper")
@ComponentScan(basePackages = {"com.xiyoulinux.joinadmin", "org.n3r.idworker"})
@EnableDiscoveryClient
@EnableHystrix
@EnableDubbo(scanBasePackages = "com.xiyoulinux.joinadmin.service.impl")
public class JoinAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(JoinAdminApplication.class, args);
    }
}
