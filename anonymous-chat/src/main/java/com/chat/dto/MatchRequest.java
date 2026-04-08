package com.chat.dto;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 匹配请求 DTO
 */
@Data
public class MatchRequest {
    
    @NotNull(message = "账号 ID 不能为空")
    private Long accountId;
    
    /**
     * 性别偏好：0-不限，1-男，2-女
     */
    @Min(value = 0, message = "性别偏好值无效")
    @Max(value = 2, message = "性别偏好值无效")
    private Integer genderPreference = 0;
    
    @Min(value = 1, message = "最小年龄不能小于 1")
    @Max(value = 100, message = "最大年龄不能大于 100")
    private Integer minAge = 18;
    
    @Min(value = 1, message = "最小年龄不能小于 1")
    @Max(value = 100, message = "最大年龄不能大于 100")
    private Integer maxAge = 100;
}
