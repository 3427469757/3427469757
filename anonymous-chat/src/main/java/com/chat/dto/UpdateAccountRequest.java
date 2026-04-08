package com.chat.dto;

import com.chat.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user account information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {

    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    private String nickname;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age cannot exceed 150")
    private Integer age;

    private Gender gender;

    @Size(max = 100, message = "Region cannot exceed 100 characters")
    private String region;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
}
