package com.xiyoulinux;

//import com.zk.auth.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author CoderZk
 */
@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients(basePackageClasses = {
//        AuthService.class
//})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class);
    }
}
