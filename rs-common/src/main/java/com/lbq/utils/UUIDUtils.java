package com.lbq.utils;

import java.util.UUID;

/**
 * uuid生成
 *
 * @Author: lbq
 * @Date: 2024/1/8
 * @Version: 1.0
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
