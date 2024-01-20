package com.lbq.filter;

import com.lbq.constants.TokenConstants;
import com.lbq.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 网关
 *
 * @Author lbq
 * @Date 2024/1/20
 * @Version: 1.0
 */
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    /**
     * 白名单
     */
    private static List<String> WHITE_URLS = Arrays.asList("/auth/login", "/auth/logout", "/auth/register");

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String path = request.getURI().getPath();
        if (WHITE_URLS.contains(path)) {
            return chain.filter(exchange);
        }
        String token = this.getToken(request);
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        if (token == null) {
            return null;
        }
        return token.replaceFirst(TokenConstants.PREFIX, "");
    }
}
