package com.lbq.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * redisson客户端配置
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
@Component
public class RedissonConfig {

    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.useSingleServer().setPassword("xxxx");
        return Redisson.create(config);
    }
}
