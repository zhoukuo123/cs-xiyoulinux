package com.xiyoulinux.activity.comment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qkm
 */
@SpringBootApplication
@EnableRetry
@MapperScan(basePackages = "com.xiyoulinux.activity.comment.mapper")
@EnableSwagger2
public class ActivityCommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityCommentApplication.class,args);
    }
}
