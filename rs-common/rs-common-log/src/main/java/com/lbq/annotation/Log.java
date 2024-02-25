package com.lbq.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @Author lbq
 * @Date 2024/2/25
 * @Version: 1.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String title() default "";
}
