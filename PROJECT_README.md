# 人事管理系统 - 项目文档

## 项目概述

本项目是一个基于 **Vue 2 + Element-UI** 前端和 **Spring Boot + MyBatis-Plus** 后端的人事管理系统。系统采用前后端分离架构，实现了基于角色（RBAC）的权限管理功能，不同角色的用户可以展示不同的管理组件和功能。

---

## 技术栈

### 前端 (hr-front/)
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 2.7.16 | 核心框架 |
| Vue Router | 3.6.5 | 路由管理 |
| Vuex | 3.6.2 | 状态管理 |
| Element-UI | 2.15.14 | UI 组件库 |
| Axios | 0.27.2 | HTTP 请求 |

### 后端 (hr-background/)
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 核心框架 |
| MyBatis-Plus | 4.2.0 | ORM 框架 |
| H2 Database | - | 内存数据库 |
| Swagger | 3.0.0 | API 文档 |
| BCrypt | - | 密码加密 |

---

## 目录结构

```
hr/
├── hr-background/                    # 后端 Java 项目
│   ├── src/main/java/com/hr/
│   │   ├── HrApplication.java        # 启动类
│   │   ├── config/                   # 配置类
│   │   │   ├── SecurityConfig.java   # 安全配置
│   │   │   ├── CorsConfig.java       # 跨域配置
│   │   │   └── MybatisPlusConfig.java
│   │   ├── controller/               # 控制器层
│   │   │   ├── UserController.java   # 用户接口
│   │   │   ├── RoleController.java   # 角色接口
│   │   │   ├── PermissionController.java # 权限接口
│   │   │   ├── DepartmentController.java # 部门接口
│   │   │   └── EmployeeController.java   # 员工接口
│   │   ├── service/                  # 服务层
│   │   ├── mapper/                   # 数据访问层
│   │   └── entity/                   # 实体类
│   └── src/main/resources/
│       ├── application.yml           # 配置文件
│       ├── schema.sql                # 数据库初始化脚本
│       └── mapper/*.xml              # MyBatis 映射
│
└── hr-front/                         # 前端 Vue 项目
    ├── src/
    │   ├── api/index.js              # API 接口封装
    │   ├── layout/Index.vue          # 布局组件（侧边栏）
    │   ├── router/index.js           # 路由配置
    │   ├── store/index.js            # Vuex 状态管理
    │   ├── utils/request.js          # Axios 封装
    │   ├── views/                    # 页面组件
    │   │   ├── Login.vue             # 登录页
    │   │   ├── Dashboard.vue         # 首页
    │   │   ├── employee/index.vue    # 员工管理
    │   │   ├── department/index.vue  # 部门管理
    │   │   ├── user/index.vue        # 用户管理
    │   │   ├── role/index.vue        # 角色管理
    │   │   └── permission/index.vue  # 权限管理
    │   ├── App.vue
    │   └── main.js                   # 入口文件（路由守卫）
    └── public/
```

---

## 权限管理实现详解

### 一、数据库设计

系统采用标准的 **RBAC（Role-Based Access Control）** 模型，包含 6 张核心表：

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│    user     │────▶│    user_role     │◀────│    role     │
│   (用户)     │     │  (用户 - 角色关联)  │     │   (角色)     │
└─────────────┘     └──────────────────┘     └─────────────┘
                                                    │
                                                    │
                     ┌──────────────────┐           │
                     │ role_permission  │◀──────────┘
                     │ (角色 - 权限关联)   │
                     └──────────────────┘
                                                    │
                                                    │
                     ┌─────────────┐                │
                     │ permission  │◀───────────────┘
                     │   (权限)     │
                     └─────────────┘
```

#### 核心表说明

| 表名 | 功能 | 关键字段 |
|------|------|----------|
| `user` | 用户表 | id, username, password, status |
| `role` | 角色表 | id, role_name, **role_code** (ADMIN/HR_MANAGER/EMPLOYEE) |
| `permission` | 权限表 | id, perm_name, **perm_code**, **perm_type**(1=菜单/2=按钮/3=接口) |
| `user_role` | 用户 - 角色关联 | user_id, role_id |
| `role_permission` | 角色 - 权限关联 | role_id, permission_id |

#### 角色定义

| 角色编码 | 角色名称 | 权限说明 |
|----------|----------|----------|
| `ADMIN` | 超级管理员 | 拥有所有权限 |
| `HR_MANAGER` | 部门负责人 | 可访问员工管理、部门管理 |
| `EMPLOYEE` | 普通员工 | 只能访问员工列表 |

---

### 二、权限控制流程

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           权限控制完整流程                                   │
└─────────────────────────────────────────────────────────────────────────────┘

  1. 用户登录
     ┌──────────┐                              ┌──────────┐
     │  前端     │                              │  后端     │
     │  Login   │                              │UserController│
     │  .vue    │──── POST /api/user/login ──▶│ .login() │
     └──────────┘                              └────┬─────┘
                                                    │
                         ┌──────────────────────────┘
                         │ 查询用户 + 角色 + 权限
                         ▼
              ┌───────────────────────┐
              │ 返回数据结构：          │
              │ {                     │
              │   user: {...},        │
              │   roleCodes: [],      │  ◀── 用于前端判断菜单显示
              │   permCodes: []       │  ◀── 用于按钮权限控制
              │ }                     │
              └───────────────────────┘

  2. 前端存储
     ┌──────────┐
     │  Vuex    │
     │  Store   │──▶ localStorage 持久化
     └──────────┘
     - token
     - userInfo
     - roleCodes

  3. 菜单渲染
     ┌─────────────────────┐
     │ layout/Index.vue    │
     │ 侧边栏组件           │
     │                     │
     │ v-if="isAdmin"      │  ◀── 根据角色编码条件渲染
     │ - 部门管理           │
     │ - 权限管理           │
     └─────────────────────┘

  4. 路由守卫
     ┌──────────┐
     │  main.js │
     │  before   │
     │  Each()   │──▶ 检查 token，未登录跳转登录页
     └──────────┘
```

---

### 三、前端实现细节

#### 3.1 状态管理 (store/index.js)

```javascript
state: {
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  roleCodes: JSON.parse(localStorage.getItem('roleCodes') || '[]'),
  sidebarCollapsed: false
},

getters: {
  // 判断是否是管理员（ADMIN 或 HR_MANAGER）
  isAdmin: state => {
    const codes = state.roleCodes || []
    return codes.includes('ADMIN') || codes.includes('HR_MANAGER')
  },
  // 判断是否是普通员工
  isEmployee: state => {
    const codes = state.roleCodes || []
    return codes.includes('EMPLOYEE') && 
           !codes.includes('ADMIN') && 
           !codes.includes('HR_MANAGER')
  }
}
```

#### 3.2 侧边栏菜单权限 (layout/Index.vue)

```vue
<el-menu>
  <el-menu-item index="/dashboard">
    <i class="el-icon-s-home"></i>
    <span>首页</span>
  </el-menu-item>
  
  <el-menu-item index="/employee">
    <i class="el-icon-user"></i>
    <span>员工管理</span>
  </el-menu-item>
  
  <!-- 部门管理：仅管理员和部门负责人可见 -->
  <el-menu-item v-if="isAdmin" index="/department">
    <i class="el-icon-s-order"></i>
    <span>部门管理</span>
  </el-menu-item>
  
  <el-menu-item index="/user">
    <i class="el-icon-s-custom"></i>
    <span>用户管理</span>
  </el-menu-item>
  
  <el-menu-item index="/role">
    <i class="el-icon-s-check"></i>
    <span>角色管理</span>
  </el-menu-item>
  
  <!-- 权限管理：仅管理员和部门负责人可见 -->
  <el-menu-item v-if="isAdmin" index="/permission">
    <i class="el-icon-s-flag"></i>
    <span>权限管理</span>
  </el-menu-item>
</el-menu>
```

#### 3.3 登录流程 (Login.vue)

```javascript
handleLogin() {
  this.$refs.loginForm.validate(valid => {
    if (valid) {
      login(this.loginForm).then(res => {
        // res.data = { user, roleCodes, permCodes }
        this.$store.dispatch('login', res.data).then(() => {
          this.$message.success('登录成功')
          this.$router.push('/')
        })
      })
    }
  })
}
```

#### 3.4 路由守卫 (main.js)

```javascript
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path === '/login') {
    // 已登录访问登录页，重定向到首页
    if (token) next('/')
    else next()
  } else {
    // 访问其他页面，检查是否登录
    if (token) next()
    else next('/login')
  }
})
```

---

### 四、后端实现细节

#### 4.1 用户登录接口 (UserController.java)

```java
@PostMapping("/login")
public Result<Map<String, Object>> login(@RequestBody User user) {
    User loginUser = userService.login(user.getUsername(), user.getPassword());
    if (loginUser != null) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", loginUser);
        result.put("roleCodes", loginUser.getRoleCodes());  // 角色编码
        
        // 获取用户权限
        List<Permission> permissions = permissionService.listByUserId(loginUser.getId());
        List<String> permCodes = permissions.stream()
                .map(Permission::getPermCode)
                .collect(Collectors.toList());
        result.put("permCodes", permCodes);  // 权限编码
        
        return Result.success(result);
    }
    return Result.error("用户名或密码错误");
}
```

#### 4.2 权限查询 SQL (PermissionMapper.xml)

```xml
<select id="selectByUserId" resultType="com.hr.entity.Permission">
    SELECT DISTINCT p.*
    FROM permission p
    INNER JOIN role_permission rp ON p.id = rp.permission_id
    INNER JOIN user_role ur ON rp.role_id = ur.role_id
    WHERE ur.user_id = #{userId}
</select>
```

#### 4.3 获取用户菜单树 (PermissionController.java)

```java
@GetMapping("/menu/{userId}")
public Result<List<Permission>> menuTree(@PathVariable Long userId) {
    List<Permission> userPerms = permissionService.listByUserId(userId);
    
    // 只保留菜单类型 (permType=1)
    List<Permission> menuPerms = userPerms.stream()
            .filter(p -> p.getPermType() == 1)
            .collect(Collectors.toList());

    // 构建树形结构
    return Result.success(menuPerms.stream()
            .filter(p -> p.getParentId() == 0 || p.getParentId() == null)
            .peek(p -> p.setChildren(buildChildren(p.getId(), menuPerms)))
            .collect(Collectors.toList()));
}
```

---

### 五、不同角色的菜单展示差异

| 菜单项 | ADMIN | HR_MANAGER | EMPLOYEE |
|--------|-------|------------|----------|
| 首页 | ✅ | ✅ | ✅ |
| 员工管理 | ✅ | ✅ | ✅ |
| 部门管理 | ✅ | ✅ | ❌ |
| 用户管理 | ✅ | ✅ | ✅ |
| 角色管理 | ✅ | ✅ | ✅ |
| 权限管理 | ✅ | ❌ | ❌ |

> **注意**: 当前实现中，普通员工 (EMPLOYEE) 虽然可以看到部分菜单，但具体页面的数据访问权限还需后端接口层面进行控制。

---

## API 接口一览

### 用户接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/login | 用户登录 |
| GET | /api/user/list | 获取用户列表 |
| GET | /api/user/{id} | 根据 ID 查询用户 |
| POST | /api/user | 新增用户 |
| PUT | /api/user | 修改用户 |
| DELETE | /api/user/{id} | 删除用户 |

### 角色接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/role/list | 获取角色列表 |
| GET | /api/role/tree | 获取角色树 |
| POST | /api/role | 新增角色 |
| PUT | /api/role | 修改角色 |
| DELETE | /api/role/{id} | 删除角色 |

### 权限接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/permission/list | 获取权限列表 |
| GET | /api/permission/tree | 获取权限树 |
| GET | /api/permission/menu/{userId} | 获取用户菜单树 |
| GET | /api/permission/user/{userId} | 获取用户权限 |
| POST | /api/permission | 新增权限 |
| PUT | /api/permission | 修改权限 |
| DELETE | /api/permission/{id} | 删除权限 |

---

## 快速开始

### 后端启动
```bash
cd hr-background
mvn spring-boot:run
# 访问 http://localhost:8080
# Swagger 文档：http://localhost:8080/swagger-ui/
```

### 前端启动
```bash
cd hr-front
npm install
npm run dev
# 访问 http://localhost:8081
```

### 默认账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 超级管理员 |
| hr1 | 123456 | 部门负责人 |
| user1 | 123456 | 普通员工 |

---

## 扩展建议

1. **按钮级权限控制**: 当前实现了菜单级权限，可扩展到按钮级别（通过 `v-permission` 指令）
2. **动态路由**: 可根据用户权限动态生成路由表，实现更细粒度的访问控制
3. **接口权限拦截**: 后端添加权限拦截器，验证用户权限编码
4. **数据权限**: 实现部门数据隔离，部门负责人只能查看本部门数据

---

## 总结

本系统的权限管理核心思想：

1. **后端**: 通过 `user -> user_role -> role -> role_permission -> permission` 的关联关系，查询用户拥有的所有权限
2. **前端**: 登录时获取 `roleCodes`，通过 Vuex 的 `getters` 提供 `isAdmin`、`isEmployee` 等判断方法
3. **UI 展示**: 在侧边栏组件中使用 `v-if="isAdmin"` 条件渲染，控制不同角色显示不同的菜单项

这种实现方式简单直观，适合中小型系统的权限管理需求。
