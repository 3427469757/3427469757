package com.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 匿名客户端实体
 * 存储用户唯一标识码 (clientId)
 */
@Data
@Entity
@Table(name = "anonymous_client")
public class AnonymousClient {

    @Id
    @Column(name = "client_id", length = 64, nullable = false)
    private String clientId;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public AnonymousClient() {
    }

    public AnonymousClient(String clientId) {
        this.clientId = clientId;
    }
}
