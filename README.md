# 🏡 东软智慧社区项目（Smart Community Platform）

## 📘 项目简介
本项目为 **东软智慧社区平台（Neusoft Smart Community）**，  
旨在为社区居民提供便捷的生活服务与物业管理系统。  

系统采用前后端分离架构，  
支持 **用户端、商户端、社区端与管理后台** 四大应用角色。  

---

## 🧠 技术栈

### 前端
- Vue 3.2 + Vite 4
- Element Plus + Pinia + Axios  
- 模块化开发，每人负责独立视图与 API 调用  

### 后端
- Spring Boot 3.x  
- MyBatis-Plus  
- Redis 缓存  
- SLF4J + Logback 日志系统  
- JWT 用户认证机制  
- 全局异常处理 + 统一响应结构  

### 数据库
- MySQL（按功能模块分表设计）

---

## ⚙️ 不使用的工具（已禁用）
❌ Swagger  
❌ Postman  
❌ Mock.js  

改为：
- ✅ 使用 **Axios** 调试 REST 接口  
- ✅ 使用 **SLF4J 日志** 跟踪请求链路与调试数据  

---

## 🧩 模块与成员职责

| 成员 | 模块 | 说明 |
|------|------|------|
| **A** | 用户中心模块 | 注册、登录、个人资料、钱包充值/转账、账单查询 |
| **B** | 商城与订单模块 | 商品管理、购物车、订单、支付、促销活动 |
| **C** | 社区服务模块 | 公告、访客登记、车位、报修、投诉、缴费 |
| **D** | 管理后台模块 | 管理员、角色、门店、区域、数据统计 |
| **E** | 系统公共模块 | JWT鉴权、Redis缓存、日志、异常处理、部署 |

---

## 📁 项目目录结构

```
smart-community/
│
├── backend/
│   ├── src/main/java/com/neusoft/community/
│   │   ├── common/
│   │   ├── user/
│   │   ├── shop/
│   │   ├── community/
│   │   ├── admin/
│   │   └── CommunityApplication.java
│   ├── src/main/resources/
│   │   ├── mapper/*.xml
│   │   ├── application.yml
│   │   └── logback-spring.xml
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── stores/
│   │   ├── views/
│   │   ├── router/
│   │   └── main.js
│   ├── vite.config.js
│   └── package.json
│
└── README.md
```

---

## 🔐 后端结构说明

### 统一响应体
```java
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}
```

### 日志（SLF4J）
```java
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO dto) {
        log.info("用户登录请求：{}", dto.getPhone());
        return Result.success("登录成功");
    }
}
```

### 全局异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.fail(e.getMessage());
    }
}
```

---

## 🧾 前端结构说明

### Axios 封装
```js
import axios from 'axios';
import { useUserStore } from '@/stores/user';

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
});

request.interceptors.request.use(config => {
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`;
  }
  return config;
});

export default request;
```

### 模块接口调用
```js
import request from './request';
export const login = (data) => request.post('/user/login', data);
export const register = (data) => request.post('/user/register', data);
```

---

## 🚀 启动方式

### 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 启动前端
```bash
cd frontend
npm install
npm run dev
```

### 访问地址
```
http://localhost:5173     # Vue 前端
http://localhost:8080/api # 后端 REST API
```

---

## 🧩 日志与联调机制
- 前后端联调通过 **Axios 请求** 与 **SLF4J 控制台日志**
- 无需 Swagger / Mock.js / Postman
- 日志输出路径：`backend/logs/`
- 查看请求链路：`grep [INFO]`

---

## 🧱 数据库表设计概览
| 模块 | 表名 | 描述 |
|------|------|------|
| 用户中心 | `user`, `wallet`, `bill` | 用户注册、资金流水 |
| 商城模块 | `product`, `cart`, `order`, `promotion` | 商品与订单信息 |
| 社区模块 | `notice`, `visitor`, `repair`, `complaint`, `payment` | 社区事务管理 |
| 管理后台 | `admin`, `role`, `store`, `region`, `stat_log` | 管理数据与权限 |
| 系统模块 | `log_record`, `config` | 系统日志与配置项 |

---

## 🧾 建议代码提示词（供 Cursor 使用）
```
Implement a Spring Boot controller for the /api/user module
with MyBatis-Plus service, using Result<T> wrapper and SLF4J logging.

Implement a Vue3 login page using Element Plus, connected via Axios to /api/user/login.
```

---

## 📦 部署说明
- Redis 默认运行在 `localhost:6379`
- 数据库配置在 `application.yml`
- 部署命令：
```bash
mvn clean package
java -jar target/community.jar
```

---

> ✨ 本文档为 **Cursor AI 辅助代码生成指南**。  
> 通过明确的结构、约定与日志输出，Cursor 能自动识别并生成各模块代码骨架。
