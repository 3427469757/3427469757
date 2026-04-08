package com.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 聊天消息实体
 */
@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "send_id", nullable = false)
    private Long sendId;

    @Column(name = "receive_id", nullable = false)
    private Long receiveId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "type", length = 16, nullable = false)
    private String type = "text";

    @Column(name = "is_read", nullable = false)
    private Integer isRead = 0;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    public ChatMessage() {
    }

    public ChatMessage(Long id, Long sendId, Long receiveId, String content) {
        this.id = id;
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.type = "text";
        this.isRead = 0;
    }
}
