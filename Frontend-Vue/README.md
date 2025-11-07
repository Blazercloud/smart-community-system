# 东软智慧社区前端项目

## 项目简介

基于Vue 3 + Element Plus开发的智慧社区前端系统。

## 技术栈

- **Vue 3.5** - 渐进式JavaScript框架
- **Vite** - 新一代前端构建工具
- **Element Plus** - Vue 3 UI组件库
- **Vue Router 4** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP客户端

## 快速开始

### 1. 安装依赖

```bash
cd front-end-vue
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

### 3. 访问应用

打开浏览器访问: http://localhost:5173

### 4. 构建生产版本

```bash
npm run build
```

## 项目结构

```
src/
├── api/              # API接口封装
├── assets/          # 静态资源
├── components/      # 公共组件
├── router/         # 路由配置
├── stores/         # Pinia状态管理
├── utils/          # 工具函数
├── views/          # 页面组件
│   ├── common/    # 公共页面
│   ├── user/      # 用户端
│   ├── admin/     # 管理端
│   ├── shop/      # 商城模块
│   └── community/ # 社区模块
├── App.vue         # 根组件
└── main.js         # 入口文件
```

## 功能模块

### 用户端
- 用户登录/注册
- 个人中心
- 我的钱包
- 商城购物
- 社区公告
- 社区服务

### 管理端
- 管理员登录
- 用户管理
- 门店管理
- 商品管理
- 订单管理
- 数据统计

## API配置

默认后端地址: `http://localhost:8080/api`

如需修改，编辑 `src/utils/request.js`：

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  ...
})
```

## 开发规范

1. 使用Element Plus组件库
2. 使用Pinia进行状态管理
3. API请求统一使用Axios封装
4. 路由使用Vue Router 4
5. 遵循Vue 3 Composition API规范

## 测试账号

### 用户账号
- 手机号: `13800138001`
- 密码: `user123`

### 管理员账号
- 用户名: `admin`
- 密码: `admin123`

## 注意事项

1. 确保后端服务已启动
2. 检查API接口地址配置
3. Token存储在localStorage中
4. 需要安装并运行Redis（后端要求）
