package com.lbq.service;

import com.lbq.function.CallbackFunction;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redisson工具类
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
@Component
public class RedissonService {

    private static final String LOCK_PREFIX = "RLOCK:";
    private static final long DEFAULT_WAIT_TIME = 10;
    private static final long DEFAULT_LEASE_TIME = 30;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 分布式锁
     *
     * @param lockName
     * @param waitTime
     * @param leaseTime
     * @param fun
     * @param <T>
     * @return
     */
    public <T> T tryLockExecute(String lockName, long waitTime, long leaseTime, CallbackFunction<T> fun) {
        // 获取锁
        RLock lock = redissonClient.getLock(LOCK_PREFIX + lockName);
        try {
            // 如果锁定成功，则返回true，如果锁定已设置，则返回false。
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                return fun.callback();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    /**
     * 分布式锁
     *
     * @param lockName
     * @param fun
     * @param <T>
     * @return
     */
    public <T> T tryLockExecute(String lockName, CallbackFunction<T> fun) {
        return tryLockExecute(lockName, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, fun);
    }
}
