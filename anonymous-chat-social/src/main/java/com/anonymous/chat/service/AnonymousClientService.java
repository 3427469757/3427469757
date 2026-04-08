package com.anonymous.chat.service;

import com.anonymous.chat.entity.AnonymousClient;
import com.anonymous.chat.mapper.AnonymousClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 客户端 Service 实现类
 * Client Service Implementation
 */
@Slf4j
@Service
public class AnonymousClientService {

    @Autowired
    private AnonymousClientMapper clientMapper;

    /**
     * 注册或获取客户端
     * @param clientId 客户端唯一标识
     * @return 客户端信息
     */
    public AnonymousClient registerOrGetClient(String clientId) {
        // 校验 clientId 格式（32 位字符串）
        if (clientId == null || clientId.length() != 32) {
            throw new IllegalArgumentException("clientId 格式错误，应为 32 位字符串");
        }

        // 查询是否已存在
        AnonymousClient client = clientMapper.selectById(clientId);
        if (client != null) {
            log.info("客户端已存在：{}", clientId);
            return client;
        }

        // 创建新客户端
        client = new AnonymousClient();
        client.setClientId(clientId);
        client.setCreateTime(LocalDateTime.now());
        client.setUpdateTime(LocalDateTime.now());
        clientMapper.insert(client);
        
        log.info("新客户端注册：{}", clientId);
        return client;
    }

    /**
     * 根据 clientId 获取客户端信息
     */
    public AnonymousClient getClientByClientId(String clientId) {
        return clientMapper.selectById(clientId);
    }
}
