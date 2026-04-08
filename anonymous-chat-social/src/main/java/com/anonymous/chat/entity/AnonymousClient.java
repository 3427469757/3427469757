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
 * 匿名客户端实体类
 * Anonymous Client Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("anonymous_client")
public class AnonymousClient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识码（UUID 32 位）
     */
    @TableId(value = "client_id", type = IdType.INPUT)
    private String clientId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
