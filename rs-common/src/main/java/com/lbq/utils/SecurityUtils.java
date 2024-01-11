package com.lbq.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加解密工具
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
public class SecurityUtils {

    /**
     * 加密
     *
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 验证
     *
     * @param password
     * @param encodePassword
     * @return
     */
    public static boolean verify(String password, String encodePassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, encodePassword);
    }
}
