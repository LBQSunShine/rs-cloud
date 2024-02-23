package com.lbq.utils;

import java.io.Serializable;

/**
 * 字符串拼接工具
 *
 * @Author lbq
 * @Date 2024/1/30
 * @Version: 1.0
 */
public class StringFormatUtils {
    public static String format(String text, Serializable... params) {
        if (text == null) {
            return "";
        }
        for (Serializable item : params) {
            text = text.replaceFirst("\\{}", item.toString());
        }
        return text;
    }
}
