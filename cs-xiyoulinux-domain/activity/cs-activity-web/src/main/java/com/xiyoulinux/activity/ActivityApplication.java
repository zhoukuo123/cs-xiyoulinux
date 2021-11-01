package com.xiyoulinux.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux","org.n3r.idworker"})
@MapperScan(basePackages = "com.xiyoulinux.activity.mapper")
@EnableDiscoveryClient
@EnableSwagger2
@EnableHystrix
public class ActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }
}
