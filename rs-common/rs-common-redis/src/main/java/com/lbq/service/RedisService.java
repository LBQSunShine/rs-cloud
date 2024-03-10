package com.lbq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     * @param <T>
     */
    public <T> void set(String key, T value, Long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     * @param value
     * @param timeOut
     * @param <T>
     */
    public <T> void set(String key, T value, Long timeOut) {
        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存时间
     *
     * @param key
     * @param timeOut
     * @param <T>
     * @return
     */
    public <T> boolean expire(String key, Long timeOut) {
        return redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存时间
     *
     * @param key
     * @param timeOut
     * @param timeUnit
     * @param <T>
     * @return
     */
    public <T> boolean expire(String key, Long timeOut, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeOut, timeUnit);
    }

    /**
     * 获取缓存时间
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 删除缓存对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param map
     */
    public <T> void hSet(String key, Map<String, T> map) {
        if (map != null) {
            redisTemplate.opsForHash().putAll(key, map);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void hSetValue(String key, String hKey, T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T hGetValue(String key, String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean hDeleteValue(String key, String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获取key值固定前缀的values
     *
     * @param prefix
     * @return
     */
    public <T> Cursor<T> hGetValuesWithPrefix(String prefix) {
        return redisTemplate.opsForHash().scan(prefix, ScanOptions.NONE);
    }
}
