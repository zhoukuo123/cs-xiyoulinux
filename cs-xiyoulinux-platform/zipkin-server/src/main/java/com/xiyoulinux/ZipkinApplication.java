package com.xiyoulinux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * @author CoderZk
 */
@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class ZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
}
