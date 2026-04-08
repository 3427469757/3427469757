package com.anonymous.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置类
 * Static Resource Configuration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射头像资源
        registry.addResourceHandler("/static/avatar/**")
                .addResourceLocations("classpath:/static/avatar/");
        
        // 映射其他静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
