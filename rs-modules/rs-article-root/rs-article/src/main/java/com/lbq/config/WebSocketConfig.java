package com.lbq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket
 *
 * @author lbq
 * @since 2024-07-02
 */
@Configuration
public class WebSocketConfig {

    /**
     * ServerEndpointExporter注入
     * 该Bean会自动注册使用@ServerEndpoint注解申明的WebSocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
