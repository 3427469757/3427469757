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
 * 虚拟账号实体类
 * Anonymous Account Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("anonymous_account")
public class AnonymousAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号 ID（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联客户端 ID
     */
    private String clientId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别：0 未知、1 男、2 女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 地区（省 - 市 - 区）
     */
    private String region;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 头像路径
     */
    private String avatar;

    /**
     * 逻辑删除：0 未删除、1 已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
