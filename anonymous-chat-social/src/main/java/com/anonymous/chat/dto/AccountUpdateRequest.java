package com.anonymous.chat.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 账号更新请求 DTO
 * Account Update Request DTO
 */
@Data
public class AccountUpdateRequest {

    /**
     * 客户端唯一标识
     */
    @NotBlank(message = "clientId 不能为空")
    private String clientId;

    /**
     * 账号 ID
     */
    @NotNull(message = "账号 ID 不能为空")
    private Long accountId;

    /**
     * 昵称（1-32 位）
     */
    @Size(max = 32, message = "昵称长度不能超过 32 个字符")
    private String nickname;

    /**
     * 性别：0 未知、1 男、2 女
     */
    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    private Integer gender;

    /**
     * 年龄（1-100）
     */
    @Min(value = 1, message = "年龄必须在 1-100 之间")
    @Max(value = 100, message = "年龄必须在 1-100 之间")
    private Integer age;

    /**
     * 地区
     */
    @Size(max = 64, message = "地区长度不能超过 64 个字符")
    private String region;

    /**
     * 个性签名（0-128 位）
     */
    @Size(max = 128, message = "个性签名长度不能超过 128 个字符")
    private String signature;

    /**
     * 头像路径
     */
    private String avatar;
}
