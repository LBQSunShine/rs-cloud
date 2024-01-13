package com.lbq.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应结果主体
 *
 * @Author: lbq
 * @Date: 2024/1/8
 * @Version: 1.0
 */
@Data
public class R<T> implements Serializable {

    private static final int SUCCESS = 200;
    private static final int FAIL = 500;

    private int code;
    private String message;
    private T data;

    public static <T> R<T> success() {
        return setResult(SUCCESS, null, null);
    }

    public static <T> R<T> success(String message) {
        return setResult(SUCCESS, message, null);
    }

    public static <T> R<T> success(T data) {
        return setResult(SUCCESS, null, data);
    }

    public static <T> R<T> success(String message, T data) {
        return setResult(SUCCESS, message, data);
    }

    public static <T> R<T> fail() {
        return setResult(FAIL, null, null);
    }

    public static <T> R<T> fail(String message) {
        return setResult(FAIL, message, null);
    }

    private static <T> R<T> setResult(int code, String message, T data) {
        R<T> result = new R<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
