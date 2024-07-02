package com.lbq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket
 *
 * @author lbq
 * @since 2024-07-02
 */
@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);

    /**
     * 保存连接信息
     */
    private static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        try {
            LOGGER.info("WebSocket Connecting!");
            if (session == null) {
                LOGGER.error("WebSocket Login Fail: session null!");
                return;
            }

//            Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
//            List<String> tokens = requestParameterMap.get("token");
//            if (CollectionUtils.isEmpty(tokens)) {
//                throw new AuthorizationException("WebSocket Login Fail！token null");
//            }
//            String ticket = tokens.get(0);
//            Map<String, Claim> decode = JwtUtil.decode(ticket);
//            if (decode == null) {
//                throw new AuthorizationException("WebSocket Login Fail！decode null");
//            }
//            Claim userIdClaim = decode.get(TokenConstant.USER_ID);
//            if (userIdClaim == null) {
//                throw new AuthorizationException("WebSocket Login Fail！userIdClaim null");
//            }

            if (!SESSION_MAP.containsKey(username)) {
                SESSION_MAP.put(username, session);
            }
            LOGGER.info("WebSocket Connection Success!");
        } catch (Exception e) {
            LOGGER.error("WebSocket Login Fail: Application Error!");
        }
    }

    @OnClose
    public void close(@PathParam("username") String username, Session session) {
        LOGGER.info("WebSocket Closing!");
        SESSION_MAP.remove(username);
        LOGGER.info("WebSocket Close Success!");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("WebSocket Receive Message: {}", message);
        this.sendMessage(message, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("WebSocket Error: {}", error.getMessage());
    }


    public void onClose(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.error("WebSocket Close Error: {}", e.getMessage());
        }
    }

    public void sendMessage(String message, Session session) {
        try {
            session.getAsyncRemote().sendText(message);
            LOGGER.info("WebSocket Send Message: {}", message);
        } catch (Exception e) {
            LOGGER.error("WebSocket Send Message Error: {}", e.getMessage());
        }
    }

    public boolean sendMessage(String username, String message) {
        try {
            Session session = SESSION_MAP.get(username);
            session.getAsyncRemote().sendText(message);
            LOGGER.info("WebSocket Send Message: {}", message);
            return true;
        } catch (Exception e) {
            LOGGER.error("WebSocket Send Message Error: {}", e.getMessage());
            return false;
        }
    }
}
