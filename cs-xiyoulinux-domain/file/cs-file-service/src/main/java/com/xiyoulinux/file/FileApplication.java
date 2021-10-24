package com.xiyoulinux.file;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux"})
@MapperScan(basePackages = "com.xiyoulinux.file.mapper")
@EnableDiscoveryClient
@EnableHystrix
@DubboComponentScan(basePackages = "com.xiyoulinux.file.insideImpl")
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
