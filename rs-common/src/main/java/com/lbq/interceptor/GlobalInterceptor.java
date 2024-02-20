package com.lbq.interceptor;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.lbq.constants.TokenConstants;
import com.lbq.context.BaseContext;
import com.lbq.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 拦截器
 *
 * @Author lbq
 * @Date 2024/1/21
 * @Version: 1.0
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String from = request.getHeader(TokenConstants.FROM);
        if (StringUtils.isBlank(from)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            PrintWriter writer = response.getWriter();
            writer.write("error");
            return false;
        }
        boolean hasKey = redisService.hasKey(from);
        if (!hasKey) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            PrintWriter writer = response.getWriter();
            writer.write("error");
            return false;
        }
        BaseContext.setUserId(request.getIntHeader(TokenConstants.USER_ID));
        BaseContext.setUserKey(request.getHeader(TokenConstants.USER_KEY));
        BaseContext.setUsername(request.getHeader(TokenConstants.USER_USER_NAME));
        BaseContext.setNickname(request.getHeader(TokenConstants.USER_NICK_NAME));
        redisService.delete(from);
        return true;
    }
}
