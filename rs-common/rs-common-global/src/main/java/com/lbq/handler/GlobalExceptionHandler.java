package com.lbq.handler;

import com.lbq.vo.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @Author lbq
 * @Date 2024/1/13
 * @Version: 1.0
 */
@ControllerAdvice(basePackages = {"com.lbq.controller"})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler
    public R<?> handlerException(Throwable throwable) {
        return R.fail(throwable.getMessage());
    }
}
