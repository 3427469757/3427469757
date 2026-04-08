package com.anonymous.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 * Chat Message Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_message")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息 ID（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送方账号 ID
     */
    private Long sendId;

    /**
     * 接收方账号 ID
     */
    private Long receiveId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：text 文本
     */
    private String type;

    /**
     * 阅读状态：0 未读、1 已读
     */
    private Integer isRead;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;
}
