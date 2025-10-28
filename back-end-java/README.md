# 东软智慧社区后端项目

## 项目简介

基于Spring Boot 3.x开发的智慧社区后端系统，采用前后端分离架构。

## 技术栈

- **Spring Boot 3.2.0**
- **MyBatis-Plus 3.5.5**
- **MySQL** 
- **Redis**
- **JWT**
- **Lombok**
- **SLF4J + Logback**

## 项目结构

```
src/main/java/com/neusoft/community/
├── common/              # 公共模块
│   ├── annotation/     # 注解
│   ├── config/         # 配置类
│   ├── exception/      # 异常处理
│   ├── interceptor/   # 拦截器
│   └── util/          # 工具类
├── user/               # 用户模块
│   ├── controller/     # 控制器
│   ├── entity/        # 实体类
│   ├── mapper/        # 数据访问层
│   ├── service/       # 业务逻辑层
│   └── dto/          # 数据传输对象
├── admin/              # 管理员模块
├── shop/               # 商城模块
└── community/          # 社区模块
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库配置

执行 `init.sql` 创建数据库：

```bash
mysql -u root -p < src/main/resources/init.sql
```

或使用DATABASE.md中的说明。

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_community?...
    username: root
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. 启动项目

```bash
# 进入项目目录
cd back-end-java

# 使用Maven启动
mvn spring-boot:run

# 或使用IDE直接运行CommunityApplication
```

### 5. 访问接口

- 后端API: http://localhost:8080/api
- 日志文件: `logs/community.log`

## API接口

### 用户模块

- `POST /user/register` - 用户注册
- `POST /user/login` - 用户登录
- `GET /user/info` - 获取用户信息

### 管理员模块

- `POST /admin/login` - 管理员登录
- `GET /admin/info` - 获取管理员信息

## 测试账号

### 用户账号
- 手机号: `13800138001`
- 密码: `user123`

### 管理员账号
- 用户名: `admin`
- 密码: `admin123`

## 开发规范

1. 使用Lombok简化代码
2. 统一使用Result<T>作为响应体
3. 使用SLF4J记录日志
4. 遵循RESTful API设计规范
5. 异常处理统一在GlobalExceptionHandler中处理

## 注意事项

1. 密码未加密，生产环境需使用BCrypt等加密算法
2. JWT密钥应保密，生产环境需使用环境变量
3. 数据库连接池参数根据实际情况调整
4. 日志文件会保存在项目logs目录下

