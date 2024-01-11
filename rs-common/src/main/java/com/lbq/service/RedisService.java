package com.lbq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


}
