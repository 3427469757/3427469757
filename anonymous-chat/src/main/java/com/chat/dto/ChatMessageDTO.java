package com.chat.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 聊天消息数据传输对象
 */
@Data
public class ChatMessageDTO {
    
    private Long id;
    private Long sendId;
    private String senderNickname;
    private String senderAvatar;
    private Long receiveId;
    private String content;
    private String type;
    private Integer isRead;
    private LocalDateTime createTime;
    
    public ChatMessageDTO() {
    }
    
    public static ChatMessageDTO fromEntity(com.chat.entity.ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setSendId(message.getSendId());
        dto.setReceiveId(message.getReceiveId());
        dto.setContent(message.getContent());
        dto.setType(message.getType());
        dto.setIsRead(message.getIsRead());
        dto.setCreateTime(message.getCreateTime());
        return dto;
    }
}
