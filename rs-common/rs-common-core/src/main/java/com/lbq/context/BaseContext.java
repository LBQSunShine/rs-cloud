package com.lbq.context;

import com.lbq.constants.TokenConstants;

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

    /**
     * 缓存userKey
     *
     * @param userKey
     */
    public static void setUserKey(String userKey) {
        set(TokenConstants.USER_KEY, userKey);
    }

    /**
     * 缓存userId
     *
     * @param userId
     */
    public static void setUserId(Integer userId) {
        set(TokenConstants.USER_ID, userId);
    }

    /**
     * 缓存username
     *
     * @param username
     */
    public static void setUsername(String username) {
        set(TokenConstants.USER_USER_NAME, username);
    }

    /**
     * 缓存nickname
     *
     * @param nickname
     */
    public static void setNickname(String nickname) {
        set(TokenConstants.USER_NICK_NAME, nickname);
    }

    public static String getUserKey() {
        return get(TokenConstants.USER_KEY).toString();
    }

    public static Integer getUserId() {
        return (Integer) get(TokenConstants.USER_ID);
    }

    public static String getUsername() {
        return get(TokenConstants.USER_USER_NAME).toString();
    }

    public static String getNickname() {
        return get(TokenConstants.USER_NICK_NAME).toString();
    }

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

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
