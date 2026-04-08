package com.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 匹配记录实体
 */
@Data
@Entity
@Table(name = "chat_match")
public class ChatMatch {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "gender_preference")
    private Integer genderPreference = 0;

    @Column(name = "min_age")
    private Integer minAge = 18;

    @Column(name = "max_age")
    private Integer maxAge = 100;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public ChatMessage() {
    }

    /**
     * 匹配状态枚举
     * 0: 匹配中，1: 匹配成功，2: 匹配失败，3: 已取消
     */
    public static class Status {
        public static final int MATCHING = 0;
        public static final int SUCCESS = 1;
        public static final int FAILED = 2;
        public static final int CANCELLED = 3;
    }
}
