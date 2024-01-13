package com.lbq.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文线程副本
 *
 * @Author lbq
 * @Date 2024/1/12
 * @Version: 1.0
 */
public class BaseContext {

    private static ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    private static void set(String key, Object value) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        map.put(key, value);
    }

    private static Object get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map.get(key);
    }
}
