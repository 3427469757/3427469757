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
 * 通讯录实体类
 * Chat Contact Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_contact")
public class ChatContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录 ID（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 当前账号 ID
     */
    private Long accountId;

    /**
     * 好友账号 ID
     */
    private Long friendId;

    /**
     * 好友备注（默认昵称）
     */
    private String remark;

    /**
     * 拉黑状态：0 未拉黑、1 已拉黑
     */
    private Integer blackStatus;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
