package com.anonymous.chat.common;

/**
 * 系统常量定义
 * System Constants
 */
public class Constants {

    /**
     * 每个 clientId 最大账号数量
     */
    public static final int MAX_ACCOUNTS_PER_CLIENT = 3;

    /**
     * 消息最大长度
     */
    public static final int MAX_MESSAGE_LENGTH = 100;

    /**
     * 聊天记录留存天数
     */
    public static final int MESSAGE_RETENTION_DAYS = 7;

    /**
     * 匹配超时时间（秒）
     */
    public static final int MATCH_TIMEOUT_SECONDS = 30;

    /**
     * WebSocket 连接闲置超时时间（分钟）
     */
    public static final int WEBSOCKET_IDLE_TIMEOUT_MINUTES = 30;

    /**
     * 频率限制：1 分钟内最多发起匹配次数
     */
    public static final int MAX_MATCH_PER_MINUTE = 3;

    /**
     * 频率限制：5 秒内最多发送消息数
     */
    public static final int MAX_MESSAGES_PER_5_SECONDS = 1;

    /**
     * 性别：未知
     */
    public static final Integer GENDER_UNKNOWN = 0;

    /**
     * 性别：男
     */
    public static final Integer GENDER_MALE = 1;

    /**
     * 性别：女
     */
    public static final Integer GENDER_FEMALE = 2;

    /**
     * 逻辑删除：未删除
     */
    public static final Integer NOT_DELETED = 0;

    /**
     * 逻辑删除：已删除
     */
    public static final Integer DELETED = 1;

    /**
     * 拉黑状态：未拉黑
     */
    public static final Integer NOT_BLACKED = 0;

    /**
     * 拉黑状态：已拉黑
     */
    public static final Integer BLACKED = 1;

    /**
     * 匹配状态：匹配中
     */
    public static final Integer MATCH_STATUS_MATCHING = 0;

    /**
     * 匹配状态：匹配成功
     */
    public static final Integer MATCH_STATUS_SUCCESS = 1;

    /**
     * 匹配状态：匹配失败
     */
    public static final Integer MATCH_STATUS_FAILED = 2;

    /**
     * 匹配状态：已取消
     */
    public static final Integer MATCH_STATUS_CANCELLED = 3;

    /**
     * 消息阅读状态：未读
     */
    public static final Integer MESSAGE_UNREAD = 0;

    /**
     * 消息阅读状态：已读
     */
    public static final Integer MESSAGE_READ = 1;

    /**
     * 默认头像路径前缀
     */
    public static final String DEFAULT_AVATAR_PREFIX = "/static/avatar/default/";

    /**
     * 上传头像路径前缀
     */
    public static final String UPLOAD_AVATAR_PREFIX = "/static/avatar/upload/";

    /**
     * LocalStorage key: clientId
     */
    public static final String CLIENT_ID_STORAGE_KEY = "anonymous_clientId";

    /**
     * 年龄最小值
     */
    public static final int MIN_AGE = 1;

    /**
     * 年龄最大值
     */
    public static final int MAX_AGE = 100;

    /**
     * 昵称最大长度
     */
    public static final int MAX_NICKNAME_LENGTH = 32;

    /**
     * 个性签名最大长度
     */
    public static final int MAX_SIGNATURE_LENGTH = 128;
}
