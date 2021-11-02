package com.xiyoulinux.activity.comment;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux","org.n3r.idworker"})
@MapperScan(basePackages = "com.xiyoulinux.activity.comment.mapper")
@DubboComponentScan(basePackages = "com.xiyoulinux.activity.comment")
@EnableSwagger2
@EnableHystrix
public class ActivityCommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityCommentApplication.class,args);
    }
}
