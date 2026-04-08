package com.chat.dto;

import com.chat.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user account response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountResponse {

    private Long id;
    private String userId;
    private String nickname;
    private Integer age;
    private String gender;
    private String region;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;

    public static UserAccountResponse fromEntity(UserAccount account) {
        return UserAccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .nickname(account.getNickname())
                .age(account.getAge())
                .gender(account.getGender() != null ? account.getGender().name() : null)
                .region(account.getRegion())
                .bio(account.getBio())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .isActive(account.getIsActive())
                .build();
    }
}
