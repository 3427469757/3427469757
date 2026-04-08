package com.anonymous.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Anonymous Chat Social Application
 * 匿名社交聊天系统启动类
 */
@SpringBootApplication
@MapperScan("com.anonymous.chat.mapper")
@EnableScheduling
public class AnonymousChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousChatApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Anonymous Chat Social Started!");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
