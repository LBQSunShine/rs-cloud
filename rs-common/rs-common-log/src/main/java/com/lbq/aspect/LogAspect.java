package com.lbq.aspect;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.lbq.annotation.Log;
import com.lbq.constants.StatusConstants;
import com.lbq.service.AsyncLogService;
import com.lbq.vo.OperLogVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志aop
 *
 * @Author lbq
 * @Date 2024/2/25
 * @Version: 1.0
 */
@Aspect
@Component
public class LogAspect {

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new NamedThreadLocal<>("Cost Time");

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            OperLogVo operLogVo = new OperLogVo();
            operLogVo.setStatus(StatusConstants.SUCCESS);
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String requestURI = request.getRequestURI();
            requestURI = requestURI.length() > 255 ? requestURI.substring(0, 255) : requestURI;
            operLogVo.setOperUrl(requestURI);
            String username = "admin";
            if (StringUtils.isNotBlank(username)) {
                operLogVo.setOperBy(username);
            }

            if (e != null) {
                operLogVo.setStatus(StatusConstants.FAIL);
                String message = e.getMessage();
                message = message.length() > 1000 ? message.substring(0, 1000) : message;
                operLogVo.setErrorMsg(message);
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLogVo.setMethod(className + "." + methodName + "()");
            // 设置标题
            operLogVo.setTitle(controllerLog.title());
            // 设置请求方式
            operLogVo.setRequestMethod(request.getMethod());
            // 设置消耗时间
            operLogVo.setCostTime(System.currentTimeMillis() - TIME_THREAD_LOCAL.get());
            // 保存数据库
            asyncLogService.addOperLog(operLogVo);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            TIME_THREAD_LOCAL.remove();
        }
    }
}
