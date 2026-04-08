# 数据库配置说明

## SQLite + DBeaver 配置

本项目已配置为使用 SQLite 数据库，可直接通过 DBeaver 连接和管理数据。

### 1. 配置文件位置
`src/main/resources/application.properties`

### 2. 当前数据库配置
```properties
# SQLite 数据库文件路径 (请根据实际路径修改)
spring.datasource.url=jdbc:sqlite:E:/DBeaver/test.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.datasource.username=
spring.datasource.password=

# JPA 配置
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis 配置 (用于匹配队列、频率限制等)
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.database=0
```

### 3. DBeaver 连接步骤

#### 方法一：直接打开数据库文件
1. 打开 DBeaver
2. 点击"新建数据库连接"
3. 选择 SQLite 数据库
4. 浏览并选择数据库文件：`E:/DBeaver/test.db`
5. 点击"完成"

#### 方法二：使用 JDBC URL
1. 打开 DBeaver
2. 点击"新建数据库连接"
3. 选择 SQLite 数据库
4. 在 JDBC URL 中输入：`jdbc:sqlite:E:/DBeaver/test.db`
5. 点击"完成"

### 4. 数据库初始化

#### 自动初始化（推荐）
启动 Spring Boot 应用后，JPA 会自动创建表结构（`ddl-auto=update`）。

#### 手动初始化
如需手动创建表，可执行 `src/main/resources/db/schema-sqlite.sql` 中的 SQL 脚本。

### 5. 切换数据库

#### 切换到 H2（开发测试）
取消注释以下配置：
```properties
# spring.datasource.url=jdbc:h2:mem:chatdb
# spring.datasource.driverClassName=org.h2.Driver
# spring.datasource.username=sa
# spring.datasource.password=
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

#### 切换到 MySQL（生产环境）
取消注释以下配置：
```properties
# spring.datasource.url=jdbc:mysql://localhost:3306/anonymous_chat?useSSL=false&serverTimezone=UTC
# spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
# spring.datasource.username=root
# spring.datasource.password=yourpassword
# spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### 6. 注意事项

1. **数据库文件路径**：Windows 路径使用正斜杠 `/` 或双反斜杠 `\\`
   - 正确：`E:/DBeaver/test.db` 或 `E:\\DBeaver\\test.db`
   - 错误：`E:\DBeaver\test.db`

2. **Redis 服务**：确保本地 Redis 服务已启动（端口 6379）
   ```bash
   # Windows 启动 Redis
   redis-server.exe
   
   # Linux/Mac 启动 Redis
   redis-server
   ```

3. **首次运行**：首次运行时会自动创建数据库文件和表结构

4. **查看数据**：可在 DBeaver 中查看以下表：
   - `anonymous_client` - 客户端标识表
   - `anonymous_account` - 虚拟账号表
   - `chat_match` - 匹配记录表
   - `chat_message` - 聊天消息表
   - `chat_contact` - 通讯录表

### 7. 常见问题

#### Q: 数据库文件不存在？
A: 首次启动应用时会自动创建。如需手动创建，可在 DBeaver 中新建 SQLite 数据库。

#### Q: 表结构未创建？
A: 检查 `spring.jpa.hibernate.ddl-auto` 是否为 `update`，或手动执行 schema-sqlite.sql。

#### Q: Redis 连接失败？
A: 确保 Redis 服务已启动，或暂时注释 Redis 相关代码（会影响匹配功能）。

#### Q: 如何清空数据？
A: 删除 `E:/DBeaver/test.db` 文件，重启应用即可重新创建。
