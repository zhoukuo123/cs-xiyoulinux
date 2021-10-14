package com.xiyoulinux.activity.comment;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux"})
@MapperScan(basePackages = "com.xiyoulinux.activity.comment.mapper")
@DubboComponentScan(basePackages = "com.xiyoulinux.activity.comment")
@EnableSwagger2
public class ActivityCommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityCommentApplication.class,args);
    }
}
