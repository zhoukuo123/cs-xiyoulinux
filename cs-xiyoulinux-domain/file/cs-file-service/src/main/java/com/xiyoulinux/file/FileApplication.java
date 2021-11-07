package com.xiyoulinux.file;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qkm
 */
@SpringBootApplication(scanBasePackages = {"com.xiyoulinux","org.n3r.idworker"})
@MapperScan(basePackages = "com.xiyoulinux.file.mapper")
@EnableDiscoveryClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
