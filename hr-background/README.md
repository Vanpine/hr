# 人事管理系统

基于 Spring Boot + MyBatis-Plus 的人事管理系统，包含员工管理、部门管理、用户和权限管理功能。

## 技术栈

- **后端框架**: Spring Boot 2.7.18
- **数据库框架**: MyBatis-Plus 3.5.3.1
- **数据库**: MySQL 8.0
- **JDK**: 11

## 功能模块

| 模块 | 功能 |
|------|------|
| 员工管理 | 员工增删改查、分页查询、批量删除 |
| 部门管理 | 部门增删改查、树形结构展示 |
| 用户管理 | 用户登录、增删改查 |
| 角色管理 | 角色增删改查、用户角色关联 |
| 权限管理 | 权限增删改查、树形结构、角色权限关联 |

## 快速开始

### 1. 数据库初始化

```bash
mysql -u root -p < src/main/resources/schema.sql
```

### 2. 修改数据库配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hr_system?...
    username: your_username
    password: your_password
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

### 4. 访问接口

项目启动后访问：http://localhost:8080

## API 接口

### 员工管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/employee/page | 分页查询员工 |
| GET | /api/employee/{id} | 根据 ID 查询员工 |
| POST | /api/employee | 新增员工 |
| PUT | /api/employee | 修改员工 |
| DELETE | /api/employee/{id} | 删除员工 |

### 部门管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/department/list | 获取所有部门 |
| GET | /api/department/tree | 获取部门树形结构 |
| GET | /api/department/{id} | 根据 ID 查询部门 |
| POST | /api/department | 新增部门 |
| PUT | /api/department | 修改部门 |
| DELETE | /api/department/{id} | 删除部门 |

### 用户管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/user/login | 用户登录 |
| GET | /api/user/list | 获取用户列表 |
| GET | /api/user/{id} | 根据 ID 查询用户 |
| POST | /api/user | 新增用户 |
| PUT | /api/user | 修改用户 |
| DELETE | /api/user/{id} | 删除用户 |

### 角色管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/role/list | 获取角色列表 |
| GET | /api/role/{id} | 根据 ID 查询角色 |
| POST | /api/role | 新增角色 |
| PUT | /api/role | 修改角色 |
| DELETE | /api/role/{id} | 删除角色 |

### 权限管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/permission/list | 获取权限列表 |
| GET | /api/permission/tree | 获取权限树形结构 |
| GET | /api/permission/{id} | 根据 ID 查询权限 |
| POST | /api/permission | 新增权限 |
| PUT | /api/permission | 修改权限 |
| DELETE | /api/permission/{id} | 删除权限 |

## 测试数据

系统初始化后包含以下测试数据：

- **默认用户**: admin / 123456
- **部门**: 总公司、技术部、人事部、财务部、市场部等
- **员工**: 张三、李四、王五等

## 项目结构

```
hr-system/
├── src/main/java/com/hr/
│   ├── HrApplication.java      # 启动类
│   ├── config/                  # 配置类
│   │   ├── CorsConfig.java     # 跨域配置
│   │   └── MybatisPlusConfig.java # MyBatis-Plus 配置
│   ├── controller/              # 控制器层
│   │   ├── Result.java         # 统一返回结果
│   │   ├── DepartmentController.java
│   │   ├── EmployeeController.java
│   │   ├── PermissionController.java
│   │   ├── RoleController.java
│   │   └── UserController.java
│   ├── service/                 # 服务层
│   │   ├── *.java              # 服务接口
│   │   └── impl/               # 服务实现
│   ├── mapper/                  # Mapper 层
│   │   └── *.java              # Mapper 接口
│   └── entity/                  # 实体类
│       ├── Department.java
│       ├── Employee.java
│       ├── Permission.java
│       ├── Role.java
│       ├── User.java
│       ├── RolePermission.java
│       └── UserRole.java
├── src/main/resources/
│   ├── application.yml          # 配置文件
│   ├── schema.sql               # 数据库初始化脚本
│   └── mapper/                  # MyBatis XML 映射文件
└── pom.xml                      # Maven 配置
```
