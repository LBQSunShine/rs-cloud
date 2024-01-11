package com.lbq.function;

/**
 * 回调函数
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
public interface CallbackFunction<T> {

    /**
     * 实现逻辑
     *
     * @return
     * @throws Exception
     */
    T callback() throws Exception;
}
