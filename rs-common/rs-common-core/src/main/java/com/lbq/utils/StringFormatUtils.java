package com.lbq.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public static <T> List<T> stringToList(String text, String split) {
        List<T> list = new ArrayList<>();
        if (text == null || "".equals(text.trim())) {
            return list;
        }
        String[] strings = text.split(split);
        for (String item : strings) {
            list.add((T) item);
        }
        return list;
    }
}
