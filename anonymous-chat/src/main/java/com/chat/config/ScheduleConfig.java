package com.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置
 * 启用 Spring 定时任务功能
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {
    // 定时任务已在 ChatService 中定义
    // - cleanupOldMessages: 每天凌晨 2 点清理 7 天前的消息
}
