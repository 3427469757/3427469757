# Anonymous Chat Application

一个基于 Spring Boot 的企业级匿名聊天后端系统。

## 功能特性

- **匿名用户标识**: 用户访问页面时生成唯一标识码，用于识别用户身份
- **多账号管理**: 每个用户可以创建最多 3 个账号
- **账号限制**: 达到上限后需删除多余账号才能新建
- **个人资料编辑**: 支持编辑昵称、年龄、性别、地区、个人简介等信息
- **软删除机制**: 账号采用软删除，数据可恢复

## 技术栈

- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **数据库**: H2 (开发环境) / MySQL (生产环境)
- **ORM**: Spring Data JPA
- **验证**: Hibernate Validator
- **工具**: Lombok

## 项目结构

```
anonymous-chat/
├── src/main/java/com/chat/
│   ├── config/              # 配置类
│   │   └── CorsConfig.java
│   ├── controller/          # 控制器层
│   │   ├── UserAccountController.java
│   │   └── UtilityController.java
│   ├── dto/                 # 数据传输对象
│   │   ├── ApiResponse.java
│   │   ├── CreateAccountRequest.java
│   │   ├── UpdateAccountRequest.java
│   │   └── UserAccountResponse.java
│   ├── entity/              # 实体类
│   │   └── UserAccount.java
│   ├── enums/               # 枚举类
│   │   └── Gender.java
│   ├── exception/           # 异常处理
│   │   ├── AccountLimitExceededException.java
│   │   ├── AccountNotFoundException.java
│   │   └── GlobalExceptionHandler.java
│   ├── repository/          # 数据访问层
│   │   └── UserAccountRepository.java
│   ├── service/             # 业务逻辑层
│   │   └── UserAccountService.java
│   ├── util/                # 工具类
│   │   └── IdGenerator.java
│   └── AnonymousChatApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## API 接口文档

### 1. 生成用户唯一标识
```
GET /api/generate-user-id
```
**响应示例:**
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "message": "Save this ID to maintain your anonymous identity"
  }
}
```

### 2. 获取用户所有账号
```
GET /api/accounts?userId={userId}
```

### 3. 获取单个账号详情
```
GET /api/accounts/{accountId}?userId={userId}
```

### 4. 创建新账号
```
POST /api/accounts?userId={userId}
Content-Type: application/json

{
  "nickname": "匿名者1号",
  "age": 25,
  "gender": "MALE",
  "region": "北京",
  "bio": "这是一个测试简介"
}
```

### 5. 更新账号信息
```
PUT /api/accounts/{accountId}?userId={userId}
Content-Type: application/json

{
  "nickname": "新的昵称",
  "age": 26,
  "region": "上海"
}
```

### 6. 删除账号
```
DELETE /api/accounts/{accountId}?userId={userId}
```

### 7. 查询账号数量限制
```
GET /api/accounts/count?userId={userId}
```
**响应示例:**
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "activeAccounts": 2,
    "maxAccounts": 3,
    "remainingSlots": 1
  }
}
```

### 8. 健康检查
```
GET /api/health
```

## 快速开始

### 前置要求
- JDK 17+
- Maven 3.6+

### 运行步骤

1. **克隆项目**
```bash
cd /workspace/anonymous-chat
```

2. **编译项目**
```bash
mvn clean compile
```

3. **运行应用**
```bash
mvn spring-boot:run
```

4. **访问应用**
- API 地址: http://localhost:8080
- H2 控制台: http://localhost:8080/h2-console

### 生产环境配置

如需使用 MySQL 数据库，修改 `application.properties`:

```properties
# 注释掉 H2 配置
# spring.datasource.url=jdbc:h2:mem:chatdb
# spring.datasource.driverClassName=org.h2.Driver
# spring.h2.console.enabled=true

# 启用 MySQL 配置
spring.datasource.url=jdbc:mysql://localhost:3306/anonymous_chat?useSSL=false&serverTimezone=UTC
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## 性别枚举值

- `MALE` - 男
- `FEMALE` - 女
- `OTHER` - 其他
- `PREFER_NOT_TO_SAY` - 不愿透露

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误或账号已达上限 |
| 404 | 账号不存在 |
| 500 | 服务器内部错误 |

## 注意事项

1. **用户标识安全**: 前端应妥善保存用户的唯一标识 (userId)，建议存储在 localStorage 或 sessionStorage 中
2. **账号限制**: 每个用户最多创建 3 个活跃账号，达到上限后需先删除账号才能新建
3. **软删除**: 删除账号采用软删除机制，数据仍保留在数据库中但标记为非活跃状态
4. **CORS 配置**: 当前配置允许所有来源访问，生产环境应指定具体域名

## 后续扩展建议

1. 添加用户认证机制 (JWT/OAuth2)
2. 实现实时聊天功能 (WebSocket)
3. 添加消息持久化和历史记录
4. 实现敏感词过滤
5. 添加举报和屏蔽功能
6. 集成 Redis 缓存提升性能
7. 添加日志记录和监控
