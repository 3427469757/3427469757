package com.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 匿名聊天应用启动类
 */
@SpringBootApplication
@EnableScheduling
public class AnonymousChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousChatApplication.class, args);
        System.out.println("========================================");
        System.out.println("  匿名聊天系统启动成功！");
        System.out.println("  WebSocket: ws://localhost:8080/ws/chat");
        System.out.println("  API: http://localhost:8080/api");
        System.out.println("========================================");
    }
}
