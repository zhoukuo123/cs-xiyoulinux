package com.xiyoulinux.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux"})
@MapperScan(basePackages = "com.xiyoulinux.activity.mapper")
@EnableDiscoveryClient
@EnableSwagger2
//@EnableAutoDataSourceProxy
public class ActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class,args);
    }
}
