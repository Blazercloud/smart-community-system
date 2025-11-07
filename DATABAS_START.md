# 数据库搭建指南

## 📋 数据库信息

- **数据库名称**: `smart_community`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`
- **数据库引擎**: `InnoDB`

## 🚀 快速开始

### 方法一：使用命令行执行SQL脚本

```bash
# 登录MySQL
mysql -u root -p

# 执行SQL脚本
source back-end-java/init.sql
```

或者：

```bash
mysql -u root -p < back-end-java/init.sql
```

### 方法二：使用MySQL Workbench

1. 打开 MySQL Workbench
2. 连接到你的MySQL服务器
3. 打开 `File > Open SQL Script`
4. 选择 `back-end-java/init.sql` 文件
5. 点击 `Execute` 按钮执行脚本

### 方法三：使用Navicat或其他数据库工具

1. 打开数据库管理工具
2. 连接MySQL服务器
3. 选择"执行SQL文件"
4. 选择 `back-end-java/init.sql` 文件
5. 执行脚本

## 📊 数据库表结构

### 用户中心模块
- `user` - 用户表
- `wallet` - 钱包表
- `bill` - 账单流水表

### 商城模块
- `product` - 商品表
- `cart` - 购物车表
- `order` - 订单表
- `promotion` - 促销活动表

### 社区模块
- `notice` - 公告表
- `visitor` - 访客登记表
- `repair` - 报修表
- `complaint` - 投诉表
- `payment` - 缴费表

### 管理后台模块
- `admin` - 管理员表
- `role` - 角色表
- `store` - 门店表
- `region` - 区域表
- `stat_log` - 统计日志表

### 系统模块
- `log_record` - 系统日志表
- `config` - 配置表

## 🔑 默认测试账号

### 管理员账号
- **用户名**: `admin`
- **密码**: `admin123`
- **备注**: 实际生产环境应使用加密密码

### 测试用户
- 手机号: `13800138001`, 密码: `user123` (张三)
- 手机号: `13800138002`, 密码: `user123` (李四)
- 手机号: `13800138003`, 密码: `user123` (王五)

**注意**: 所有用户都有对应的钱包，初始余额分别为1000元、500元、200元。

## 📝 说明

1. 密码未加密：默认账号密码均为明文，实际使用时需要在后端进行加密处理
2. 外键约束：部分表设置了外键约束，删除数据时需要注意关联关系
3. 索引优化：关键字段已添加索引，提升查询性能
4. 时间字段：使用 `DATETIME` 类型，自动记录创建和更新时间

## ⚠️ 注意事项

1. 执行脚本前请确保MySQL服务已启动
2. 建议先用备份数据库进行测试
3. 生产环境请修改默认密码
4. 如果数据库已存在，脚本会先删除并重建

## 🔧 配置数据库连接

在 Spring Boot 的 `application.yml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_community?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 📈 后续优化建议

1. 添加索引优化查询性能
2. 定期备份数据库
3. 根据业务需求添加分表策略
4. 考虑使用读写分离
5. 配置数据库连接池参数

