# 匿名社交聊天系统 - Spring Boot 后端

## 项目概述

本项目是一个企业级匿名社交聊天系统的后端实现，基于 Spring Boot 2.7.x 框架开发。系统提供完整的用户账号管理、随机匹配、即时通讯等功能，支持前后端分离架构。

## 技术栈

- **核心框架**: Spring Boot 2.7.18
- **数据库 ORM**: MyBatis Plus 3.5.3.1
- **数据库**: H2 (开发) / MySQL 8.0 (生产)
- **工具库**: Hutool 5.8.22, Lombok
- **即时通讯**: Spring WebSocket
- **Java 版本**: Java 17

## 项目结构

```
anonymous-chat-social/
├── src/main/java/com/anonymous/chat/
│   ├── AnonymousChatApplication.java    # 启动类
│   ├── common/                          # 公共模块
│   │   ├── ApiResponse.java            # 统一响应封装
│   │   ├── Constants.java              # 常量定义
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── config/                          # 配置类
│   │   ├── CorsConfig.java             # CORS 跨域配置
│   │   ├── MybatisPlusConfig.java      # MyBatis Plus 配置
│   │   └── WebConfig.java              # Web 资源配置
│   ├── controller/                      # 控制器层
│   │   ├── AccountController.java      # 账号管理接口
│   │   └── ClientController.java       # 客户端接口
│   ├── dto/                             # 数据传输对象
│   │   ├── AccountCreateRequest.java   # 账号创建请求
│   │   └── AccountUpdateRequest.java   # 账号更新请求
│   ├── entity/                          # 实体类
│   │   ├── AnonymousClient.java        # 客户端实体
│   │   ├── AnonymousAccount.java       # 虚拟账号实体
│   │   ├── ChatMatch.java              # 匹配记录实体
│   │   ├── ChatMessage.java            # 聊天消息实体
│   │   └── ChatContact.java            # 通讯录实体
│   ├── mapper/                          # 数据访问层
│   │   ├── AnonymousClientMapper.java
│   │   ├── AnonymousAccountMapper.java
│   │   ├── ChatMatchMapper.java
│   │   ├── ChatMessageMapper.java
│   │   └── ChatContactMapper.java
│   ├── service/                         # 业务逻辑层
│   │   ├── AnonymousClientService.java
│   │   └── AnonymousAccountService.java
│   ├── util/                            # 工具类
│   ├── websocket/                       # WebSocket 模块
│   └── interceptor/                     # 拦截器
├── src/main/resources/
│   ├── application.yml                  # 应用配置
│   ├── mapper/                          # MyBatis XML 映射文件
│   └── static/avatar/                   # 头像资源
│       ├── default/                     # 默认头像
│       └── upload/                      # 上传头像
├── sql/
│   └── schema.sql                       # 数据库表结构
└── pom.xml                              # Maven 配置
```

## 核心功能

### 1. 客户端唯一标识（clientId）

- 每个用户生成唯一的 32 位 UUID 作为身份凭证
- 前端存储在 localStorage 中持久化
- 一个 clientId 最多可创建 3 个虚拟账号

### 2. 虚拟账号管理

- **创建账号**: 填写昵称、性别、年龄、地区、个性签名等信息
- **编辑资料**: 随时修改账号资料
- **删除账号**: 达到上限后需删除账号才能新建
- **数量限制**: 每个 clientId 最多 3 个账号

### 3. API 接口列表

#### 客户端接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/client/generate-id` | GET | 生成新的 clientId |
| `/api/client/register` | POST | 注册或获取客户端 |
| `/api/client/validate` | GET | 验证 clientId 是否有效 |

#### 账号管理接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/account/create` | POST | 创建虚拟账号 |
| `/api/account/update` | POST | 更新账号资料 |
| `/api/account/delete` | POST | 删除账号 |
| `/api/account/list` | GET | 获取用户所有账号 |
| `/api/account/count` | GET | 统计账号数量 |
| `/api/account/detail` | GET | 获取账号详情 |

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+ (生产环境)

### 安装步骤

1. **克隆项目**
```bash
cd anonymous-chat-social
```

2. **配置数据库**

开发环境使用 H2 内存数据库（已配置），生产环境需修改 `application.yml`:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/anonymous_chat?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

3. **导入数据库表结构**

如果使用 MySQL，执行 `sql/schema.sql` 创建表结构。

4. **编译打包**
```bash
mvn clean package -DskipTests
```

5. **运行应用**
```bash
mvn spring-boot:run
```

或直接运行 jar:
```bash
java -jar target/anonymous-chat-social-1.0.0.jar
```

6. **访问服务**

应用启动后访问：http://localhost:8080

H2 控制台（开发环境）：http://localhost:8080/h2-console

## API 使用示例

### 1. 生成 clientId

```bash
GET http://localhost:8080/api/client/generate-id
```

响应:
```json
{
  "code": 200,
  "msg": "clientId 生成成功",
  "data": {
    "clientId": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
  }
}
```

### 2. 创建虚拟账号

```bash
POST http://localhost:8080/api/account/create
Content-Type: application/json

{
  "clientId": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
  "nickname": "匿名用户 001",
  "gender": 1,
  "age": 25,
  "region": "北京市 - 北京市 - 朝阳区",
  "signature": "保持神秘，保持好奇"
}
```

### 3. 查询账号数量

```bash
GET http://localhost:8080/api/account/count?clientId=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

响应:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "count": 1,
    "maxAllowed": 3,
    "canCreate": 1
  }
}
```

### 4. 获取账号列表

```bash
GET http://localhost:8080/api/account/list?clientId=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

## 统一响应格式

所有接口返回统一的 JSON 格式:

```json
{
  "code": 200,      // 状态码：200 成功、400 参数错误、403 权限不足、500 系统异常
  "msg": "操作成功", // 提示信息
  "data": {}        // 响应数据（可选）
}
```

## 核心业务规则

1. **账号数量限制**: 每个 clientId 最多创建 3 个虚拟账号
2. **删除机制**: 达到上限后需删除已有账号才能创建新账号
3. **数据隔离**: 不同 clientId 的用户数据完全隔离
4. **逻辑删除**: 账号采用逻辑删除（is_deleted 字段），便于数据回溯
5. **归属校验**: 所有账号操作均校验 clientId 与账号的归属关系

## 扩展开发

### 待实现功能（根据需求文档）

- [ ] WebSocket 即时通讯模块
- [ ] 匹配系统（随机匹配、条件匹配）
- [ ] 通讯录管理
- [ ] 聊天记录存储与查询
- [ ] 敏感词过滤
- [ ] 频率限制
- [ ] 定时清理过期消息

### 添加新模块

1. 在 `entity/` 创建实体类
2. 在 `mapper/` 创建 Mapper 接口
3. 在 `service/` 实现业务逻辑
4. 在 `controller/` 暴露 REST 接口

## 注意事项

1. **开发环境**: 默认使用 H2 内存数据库，重启后数据清空
2. **生产环境**: 需切换为 MySQL 并配置正确的数据库连接
3. **CORS 配置**: 当前允许所有来源，生产环境建议指定具体域名
4. **敏感词过滤**: 需集成敏感词库并实现过滤逻辑
5. **WebSocket**: 需实现连接管理、消息推送等完整功能

## 许可证

MIT License

## 联系方式

如有问题请提交 Issue 或联系开发团队。
