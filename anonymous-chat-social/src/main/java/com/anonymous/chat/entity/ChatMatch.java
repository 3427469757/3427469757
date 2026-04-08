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
 * 匹配记录实体类
 * Chat Match Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_match")
public class ChatMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录 ID（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发起匹配的账号 ID
     */
    private Long accountId;

    /**
     * 匹配到的账号 ID
     */
    private Long targetId;

    /**
     * 匹配状态：0 匹配中、1 匹配成功、2 匹配失败、3 已取消
     */
    private Integer status;

    /**
     * 性别偏好：0 不限、1 男、2 女
     */
    private Integer genderPreference;

    /**
     * 最小年龄
     */
    private Integer minAge;

    /**
     * 最大年龄
     */
    private Integer maxAge;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
