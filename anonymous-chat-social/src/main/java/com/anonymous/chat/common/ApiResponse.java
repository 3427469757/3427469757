package com.anonymous.chat.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * Unified API Response Wrapper
 */
@Data
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码：200成功、400参数错误、403权限不足、500系统异常
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功", null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(200, msg, data);
    }

    /**
     * 失败响应（400）
     */
    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(400, msg, null);
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> ApiResponse<T> error(Integer code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }

    /**
     * 权限不足（403）
     */
    public static <T> ApiResponse<T> forbidden(String msg) {
        return new ApiResponse<>(403, msg, null);
    }

    /**
     * 系统异常（500）
     */
    public static <T> ApiResponse<T> fail(String msg) {
        return new ApiResponse<>(500, msg, null);
    }
}
