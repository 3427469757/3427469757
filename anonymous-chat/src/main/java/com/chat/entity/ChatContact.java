package com.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 通讯录好友实体
 */
@Data
@Entity
@Table(name = "chat_contact")
public class ChatContact {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Column(name = "remark", length = 32)
    private String remark;

    @Column(name = "black_status", nullable = false)
    private Integer blackStatus = 0;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public ChatContact() {
    }

    public ChatContact(Long id, Long accountId, Long friendId) {
        this.id = id;
        this.accountId = accountId;
        this.friendId = friendId;
        this.remark = "";
        this.blackStatus = 0;
    }

    /**
     * 拉黑状态枚举
     * 0: 未拉黑，1: 已拉黑
     */
    public static class BlackStatus {
        public static final int NORMAL = 0;
        public static final int BLACKED = 1;
    }
}
