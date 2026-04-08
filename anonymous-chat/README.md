# 匿名聊天系统 - 后端服务

基于 Spring Boot 3.2 + SQLite 的匿名社交聊天系统后端服务。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: SQLite (通过 DBeaver 管理)
- **ORM**: Spring Data JPA (Hibernate)
- **即时通讯**: WebSocket (STOMP 协议)
- **缓存**: Redis (可选，用于匹配队列和频率限制)
- **工具**: Lombok, Validation API

## 项目结构

```
anonymous-chat/
├── src/main/java/com/chat/
│   ├── config/              # 配置类 (CORS, WebSocket, 定时任务)
│   ├── controller/          # RESTful 控制器
│   ├── dto/                 # 数据传输对象
│   ├── entity/              # JPA 实体类
│   ├── enums/               # 枚举类型
│   ├── exception/           # 自定义异常
│   ├── repository/          # 数据访问层
│   ├── service/             # 业务逻辑层
│   ├── util/                # 工具类
│   └── AnonymousChatApplication.java
├── src/main/resources/
│   ├── application.properties   # 应用配置
│   └── db/
│       ├── schema-sqlite.sql    # SQLite 建表脚本
│       └── README.md            # 数据库配置说明
└── pom.xml
```

## 快速开始

### 1. 环境要求

- Java 17+
- Maven 3.6+
- Redis (可选，用于高级功能)

### 2. 数据库配置

默认使用 SQLite 数据库，配置文件位于 `src/main/resources/application.properties`:

```properties
# SQLite 数据库路径 (请根据实际路径修改)
spring.datasource.url=jdbc:sqlite:E:/DBeaver/test.db
spring.datasource.driver-class-name=org.sqlite.JDBC
```

**注意**: Windows 路径请使用正斜杠 `/` 或双反斜杠 `\\`

### 3. 启动应用

```bash
cd anonymous-chat
mvn spring-boot:run
```

启动成功后会显示:
```
========================================
  匿名聊天系统启动成功！
  WebSocket: ws://localhost:8080/ws/chat
  API: http://localhost:8080/api
========================================
```

### 4. 使用 DBeaver 查看数据

1. 打开 DBeaver
2. 新建 SQLite 连接
3. 选择数据库文件：`E:/DBeaver/test.db`
4. 即可查看所有表数据

## API 接口文档

### 客户端管理

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/generate-user-id` | GET | 生成用户唯一标识 (clientId) |

### 账号管理

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/accounts` | GET | 获取用户所有账号 |
| `/api/accounts` | POST | 创建新账号 |
| `/api/accounts/{id}` | PUT | 更新账号资料 |
| `/api/accounts/{id}` | DELETE | 删除账号 |
| `/api/accounts/count` | GET | 查询账号数量 |

### 聊天消息

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/messages` | POST | 发送消息 |
| `/api/messages` | GET | 获取聊天记录 |
| `/api/messages/read` | POST | 标记消息为已读 |
| `/api/messages/unread` | GET | 获取未读消息数 |
| `/api/messages/clear` | DELETE | 清空聊天记录 |

### 匹配系统

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/match/start` | POST | 发起匹配 |
| `/api/match/cancel` | POST | 取消匹配 |

### 通讯录

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/contacts` | GET | 获取通讯录列表 |
| `/api/contacts` | DELETE | 删除好友 |
| `/api/contacts/black` | POST | 拉黑好友 |
| `/api/contacts/unblack` | POST | 解除拉黑 |

## WebSocket 使用

### 连接端点

- STOMP: `ws://localhost:8080/ws/chat`
- 原生 WebSocket: `ws://localhost:8080/ws-chat`

### 消息格式

**发送消息到服务器:**
```javascript
// 连接到 /app/chat.send
{
  "sendId": 123456789,
  "receiveId": 987654321,
  "content": "你好",
  "type": "text"
}
```

**接收服务器推送:**
```javascript
// 订阅 /topic/messages.{accountId}
{
  "id": 111222333,
  "sendId": 123456789,
  "receiveId": 987654321,
  "content": "你好",
  "type": "text",
  "isRead": 0,
  "createTime": "2024-01-01T12:00:00"
}
```

## 核心功能

### 1. 客户端唯一标识 (clientId)
- 32 位 UUID，全局唯一
- 持久化存储于前端 localStorage
- 一个 clientId 最多创建 3 个虚拟账号

### 2. 虚拟账号系统
- 独立资料：昵称、性别、年龄、地区、签名、头像
- 达到上限后需删除账号才能新建
- 软删除机制，保护数据安全

### 3. 匹配系统
- 随机匹配 / 条件匹配 (性别、年龄)
- 防重复匹配 (24 小时内不重复)
- 匹配成功自动互加好友

### 4. 即时通讯
- WebSocket 实时推送
- 消息长度限制 (100 字)
- 敏感词过滤
- 已读/未读状态

### 5. 通讯录管理
- 好友列表
- 删除好友
- 拉黑/解除拉黑

### 6. 数据维护
- 定时清理 7 天前的消息 (每天凌晨 2 点)
- 逻辑删除支持

## 统一响应格式

所有接口返回统一的 JSON 格式:

```json
{
  "code": 200,      // 状态码：200 成功，400 参数错误，403 权限不足，500 系统异常
  "msg": "操作成功", // 提示信息
  "data": {}        // 响应数据
}
```

## 切换数据库

### 切换到 H2 (开发测试)
编辑 `application.properties`,取消注释 H2 配置。

### 切换到 MySQL (生产环境)
编辑 `application.properties`,取消注释 MySQL 配置并设置密码。

## 常见问题

### Q: 数据库文件不存在？
A: 首次启动应用时会自动创建。如需手动创建，可在 DBeaver 中新建 SQLite 数据库。

### Q: Redis 连接失败？
A: Redis 是可选的。如不需要匹配队列和频率限制功能，可以暂时不配置 Redis。

### Q: WebSocket 连接失败？
A: 检查防火墙设置，确保 8080 端口开放。确认前端使用的 WebSocket 地址正确。

## License

MIT License
