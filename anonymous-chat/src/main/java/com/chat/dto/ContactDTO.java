package com.chat.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通讯录好友 DTO
 */
@Data
public class ContactDTO {
    
    private Long id;
    private Long accountId;
    private Long friendId;
    private String friendNickname;
    private String friendAvatar;
    private Integer friendGender;
    private Integer friendAge;
    private String friendRegion;
    private String remark;
    private Integer blackStatus;
    private Long unreadCount;
    private LocalDateTime createTime;
}
