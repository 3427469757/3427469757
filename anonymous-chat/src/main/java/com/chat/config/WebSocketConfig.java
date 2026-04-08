package com.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 配置类
 * 支持 STOMP 协议的即时通讯
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单的内存消息代理，用于向客户端推送消息
        config.enableSimpleBroker("/topic", "/queue");
        
        // 设置应用目的地前缀，客户端发送消息到此前的 URL 会路由到@MessageMapping 方法
        config.setApplicationDestinationPrefixes("/app");
        
        // 设置用户目的地前缀，用于点对点消息
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 STOMP 端点，客户端通过此端点建立 WebSocket 连接
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")  // 允许所有来源（生产环境应限制）
                .withSockJS();  // 启用 SockJS 回退方案
        
        // 也支持原生 WebSocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*");
    }
}
