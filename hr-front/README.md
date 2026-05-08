# 人事管理系统 - 前端

基于 Vue2 + Element-UI 的人事管理系统前端项目。

## 技术栈

- **框架**: Vue 2.7.16
- **UI 组件库**: Element-UI 2.15.14
- **路由**: Vue Router 3.6.5
- **状态管理**: Vuex 3.6.2
- **HTTP 请求**: Axios 0.27.2

## 功能模块

| 模块 | 路径 | 功能 |
|------|------|------|
| 登录 | /login | 用户登录 |
| 首页 | /dashboard | 数据统计、快捷入口 |
| 员工管理 | /employee | 员工 CRUD、分页查询、批量删除 |
| 部门管理 | /department | 部门 CRUD、树形展示 |
| 用户管理 | /user | 用户 CRUD、状态切换 |
| 角色管理 | /role | 角色 CRUD |
| 权限管理 | /permission | 权限 CRUD、树形展示 |

## 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 启动项目

```bash
npm run dev
```

项目会在 http://localhost:8081 启动

### 3. 打包

```bash
npm run build
```

## 目录结构

```
hr-vue/
├── public/
│   └── index.html
├── src/
│   ├── api/
│   │   └── index.js          # API 接口
│   ├── assets/               # 静态资源
│   ├── components/           # 公共组件
│   ├── layout/
│   │   └── Index.vue         # 布局组件
│   ├── router/
│   │   └── index.js          # 路由配置
│   ├── store/
│   │   └── index.js          # Vuex 状态管理
│   ├── utils/
│   │   └── request.js        # Axios 封装
│   ├── views/
│   │   ├── Login.vue         # 登录页
│   │   ├── Dashboard.vue     # 首页
│   │   ├── employee/         # 员工管理
│   │   ├── department/       # 部门管理
│   │   ├── user/             # 用户管理
│   │   ├── role/             # 角色管理
│   │   └── permission/       # 权限管理
│   ├── App.vue
│   └── main.js
├── package.json
├── vue.config.js
└── babel.config.js
```

## 默认账号

- 用户名：admin
- 密码：123456

## 代理配置

开发环境下，项目会代理 `/api` 请求到后端服务器 `http://localhost:8080`

修改 `vue.config.js` 可以更改代理配置。
