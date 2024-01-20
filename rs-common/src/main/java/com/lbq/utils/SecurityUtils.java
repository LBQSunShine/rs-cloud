package com.lbq.utils;

import com.lbq.constants.TokenConstants;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        if (token == null) {
            return null;
        }
        return token.replaceFirst(TokenConstants.PREFIX, "");
    }
}
