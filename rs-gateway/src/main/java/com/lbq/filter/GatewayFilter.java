package com.lbq.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import com.lbq.constants.TokenConstants;
import com.lbq.service.RedisService;
import com.lbq.utils.JwtUtils;
import com.lbq.utils.UUIDUtils;
import com.lbq.vo.R;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        if (StringUtils.isBlank(token)) {
            return this.unauthorizedResponse(exchange, "令牌不能为空!", HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> claim = JwtUtils.decode(token);
        if (CollectionUtils.isEmpty(claim)) {
            return this.unauthorizedResponse(exchange, "令牌已过期!", HttpStatus.UNAUTHORIZED);
        }
        Object key = claim.get(TokenConstants.USER_KEY);
        Object id = claim.get(TokenConstants.USER_ID);
        Object username = claim.get(TokenConstants.USER_USER_NAME);
        Object nickname = claim.get(TokenConstants.USER_NICK_NAME);
        if (key == null || id == null || username == null) {
            return this.unauthorizedResponse(exchange, "令牌验证失败!", HttpStatus.UNAUTHORIZED);
        }
        boolean hasKey = redisService.hasKey(key.toString());
        if (!hasKey) {
            return this.unauthorizedResponse(exchange, "令牌验证失败!", HttpStatus.UNAUTHORIZED);
        }
        // 设置唯一标识确保所有请求经过网关，缓存时间5分钟
        String uuid = UUIDUtils.getUUID();
        redisService.set(uuid, uuid, 5 * 60L);
        mutate
                .header(TokenConstants.USER_KEY, key.toString())
                .header(TokenConstants.USER_ID, id.toString())
                .header(TokenConstants.USER_USER_NAME, username.toString())
                .header(TokenConstants.USER_NICK_NAME, nickname.toString())
                .header(TokenConstants.FROM, uuid);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        R<?> fail = R.fail(status.value(), message);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(fail).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        if (token == null) {
            return null;
        }
        return token.replaceFirst(TokenConstants.PREFIX, "");
    }
}
