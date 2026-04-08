-- ============================================
-- 匿名社交聊天系统 - 数据库表结构
-- Anonymous Social Chat System - Database Schema
-- ============================================

-- 客户端表（anonymous_client）
DROP TABLE IF EXISTS `anonymous_client`;
CREATE TABLE `anonymous_client` (
  `client_id` varchar(64) NOT NULL COMMENT '唯一标识码（UUID 32 位）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='匿名客户端表';

-- 虚拟账号表（anonymous_account）
DROP TABLE IF EXISTS `anonymous_account`;
CREATE TABLE `anonymous_account` (
  `id` bigint(20) NOT NULL COMMENT '账号 ID（雪花算法）',
  `client_id` varchar(64) NOT NULL COMMENT '关联客户端 ID',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '性别：0 未知、1 男、2 女',
  `age` int(3) NOT NULL DEFAULT 18 COMMENT '年龄',
  `region` varchar(64) NOT NULL DEFAULT '未知地区' COMMENT '地区（省 - 市 - 区）',
  `signature` varchar(128) DEFAULT '' COMMENT '个性签名',
  `avatar` varchar(255) NOT NULL COMMENT '头像路径',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除、1 已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='虚拟账号表';

-- 匹配记录表（chat_match）
DROP TABLE IF EXISTS `chat_match`;
CREATE TABLE `chat_match` (
  `id` bigint(20) NOT NULL COMMENT '记录 ID（雪花算法）',
  `account_id` bigint(20) NOT NULL COMMENT '发起匹配的账号 ID',
  `target_id` bigint(20) NOT NULL COMMENT '匹配到的账号 ID',
  `status` tinyint(1) NOT NULL COMMENT '匹配状态：0 匹配中、1 匹配成功、2 匹配失败、3 已取消',
  `gender_preference` tinyint(1) DEFAULT 0 COMMENT '性别偏好：0 不限、1 男、2 女',
  `min_age` int(3) DEFAULT 18 COMMENT '最小年龄',
  `max_age` int(3) DEFAULT 100 COMMENT '最大年龄',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='匹配记录表';

-- 聊天消息表（chat_message）
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
  `id` bigint(20) NOT NULL COMMENT '消息 ID（雪花算法）',
  `send_id` bigint(20) NOT NULL COMMENT '发送方账号 ID',
  `receive_id` bigint(20) NOT NULL COMMENT '接收方账号 ID',
  `content` text NOT NULL COMMENT '消息内容',
  `type` varchar(16) NOT NULL DEFAULT 'text' COMMENT '消息类型：text 文本',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '阅读状态：0 未读、1 已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_send_receive` (`send_id`,`receive_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- 通讯录表（chat_contact）
DROP TABLE IF EXISTS `chat_contact`;
CREATE TABLE `chat_contact` (
  `id` bigint(20) NOT NULL COMMENT '记录 ID（雪花算法）',
  `account_id` bigint(20) NOT NULL COMMENT '当前账号 ID',
  `friend_id` bigint(20) NOT NULL COMMENT '好友账号 ID',
  `remark` varchar(32) DEFAULT '' COMMENT '好友备注（默认昵称）',
  `black_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '拉黑状态：0 未拉黑、1 已拉黑',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_friend_id` (`friend_id`),
  UNIQUE KEY `uk_account_friend` (`account_id`,`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通讯录表';
