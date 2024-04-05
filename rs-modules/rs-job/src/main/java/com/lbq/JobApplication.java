package com.lbq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 调度服务
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@SpringBootApplication
@EnableFeignClients
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
