package com.lbq.function;

/**
 * 操作回调函数
 *
 * @Author lbq
 * @Date 2024/1/30
 * @Version: 1.0
 */
public interface ActionFunction<T> {

    /**
     * 实现逻辑
     *
     * @return
     * @throws Exception
     */
    void callback(T param) throws Exception;
}
