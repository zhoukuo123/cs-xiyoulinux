package com.xiyoulinux.join;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author CoderZk
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiyoulinux.join.mapper")
@ComponentScan(basePackages = {"com.xiyoulinux.join", "org.n3r.idworker"})
@EnableDiscoveryClient
@EnableHystrix
@EnableDubbo(scanBasePackages = "com.xiyoulinux.join.service.impl")
public class JoinApplication {
    public static void main(String[] args) {
        SpringApplication.run(JoinApplication.class, args);
    }
}
