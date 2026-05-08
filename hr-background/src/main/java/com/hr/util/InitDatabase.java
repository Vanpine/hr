package com.hr.util;

import java.sql.*;

/**
 * 初始化数据库（先建表再插入数据）
 */
public class InitDatabase {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("初始化数据库 - hr_system");
        System.out.println("===========================================\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // 禁用外键检查
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // 创建表
            System.out.println("正在创建表...");

            // 部门表
            stmt.execute("DROP TABLE IF EXISTS department");
            stmt.execute(
                "CREATE TABLE `department` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门 ID'," +
                "`dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称'," +
                "`dept_code` VARCHAR(20) NOT NULL COMMENT '部门编码'," +
                "`parent_id` BIGINT DEFAULT 0 COMMENT '父部门 ID'," +
                "`sort` INT DEFAULT 0 COMMENT '排序'," +
                "`status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_dept_code` (`dept_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表'"
            );
            System.out.println("  - 部门表已创建");

            // 员工表
            stmt.execute("DROP TABLE IF EXISTS employee");
            stmt.execute(
                "CREATE TABLE `employee` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '员工 ID'," +
                "`emp_no` VARCHAR(20) NOT NULL COMMENT '员工编号'," +
                "`name` VARCHAR(50) NOT NULL COMMENT '姓名'," +
                "`gender` TINYINT DEFAULT 1 COMMENT '性别 0-女 1-男'," +
                "`phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号'," +
                "`email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱'," +
                "`dept_id` BIGINT DEFAULT NULL COMMENT '部门 ID'," +
                "`position` VARCHAR(50) DEFAULT NULL COMMENT '职位'," +
                "`entry_date` DATE DEFAULT NULL COMMENT '入职日期'," +
                "`status` TINYINT DEFAULT 1 COMMENT '状态 0-离职 1-在职'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_emp_no` (`emp_no`)," +
                "KEY `idx_dept_id` (`dept_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表'"
            );
            System.out.println("  - 员工表已创建");

            // 用户表
            stmt.execute("DROP TABLE IF EXISTS `user`");
            stmt.execute(
                "CREATE TABLE `user` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户 ID'," +
                "`username` VARCHAR(50) NOT NULL COMMENT '用户名'," +
                "`password` VARCHAR(100) NOT NULL COMMENT '密码 (BCrypt 加密)'," +
                "`real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名'," +
                "`phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号'," +
                "`email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱'," +
                "`avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像'," +
                "`status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_username` (`username`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表'"
            );
            System.out.println("  - 用户表已创建");

            // 角色表
            stmt.execute("DROP TABLE IF EXISTS `role`");
            stmt.execute(
                "CREATE TABLE `role` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色 ID'," +
                "`role_name` VARCHAR(50) NOT NULL COMMENT '角色名称'," +
                "`role_code` VARCHAR(20) NOT NULL COMMENT '角色编码'," +
                "`description` VARCHAR(255) DEFAULT NULL COMMENT '描述'," +
                "`status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_role_code` (`role_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表'"
            );
            System.out.println("  - 角色表已创建");

            // 权限表
            stmt.execute("DROP TABLE IF EXISTS `permission`");
            stmt.execute(
                "CREATE TABLE `permission` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限 ID'," +
                "`perm_name` VARCHAR(50) NOT NULL COMMENT '权限名称'," +
                "`perm_code` VARCHAR(50) NOT NULL COMMENT '权限编码'," +
                "`perm_type` TINYINT DEFAULT 1 COMMENT '权限类型 1-菜单 2-按钮 3-接口'," +
                "`parent_id` BIGINT DEFAULT 0 COMMENT '父权限 ID'," +
                "`path` VARCHAR(255) DEFAULT NULL COMMENT '路径'," +
                "`icon` VARCHAR(50) DEFAULT NULL COMMENT '图标'," +
                "`sort` INT DEFAULT 0 COMMENT '排序'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_perm_code` (`perm_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表'"
            );
            System.out.println("  - 权限表已创建");

            // 用户角色关联表
            stmt.execute("DROP TABLE IF EXISTS `user_role`");
            stmt.execute(
                "CREATE TABLE `user_role` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID'," +
                "`user_id` BIGINT NOT NULL COMMENT '用户 ID'," +
                "`role_id` BIGINT NOT NULL COMMENT '角色 ID'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表'"
            );
            System.out.println("  - 用户角色关联表已创建");

            // 角色权限关联表
            stmt.execute("DROP TABLE IF EXISTS `role_permission`");
            stmt.execute(
                "CREATE TABLE `role_permission` (" +
                "`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID'," +
                "`role_id` BIGINT NOT NULL COMMENT '角色 ID'," +
                "`permission_id` BIGINT NOT NULL COMMENT '权限 ID'," +
                "`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表'"
            );
            System.out.println("  - 角色权限关联表已创建");

            System.out.println("\n所有表已创建，正在插入数据...\n");

            // 插入数据
            // 部门数据
            stmt.execute(
                "INSERT INTO `department` (`dept_name`, `dept_code`, `parent_id`, `sort`, `status`) VALUES " +
                "('总公司', 'HEAD', 0, 1, 1), " +
                "('技术部', 'TECH', 1, 1, 1), " +
                "('人事部', 'HR', 1, 2, 1), " +
                "('财务部', 'FIN', 1, 3, 1), " +
                "('市场部', 'MARKET', 1, 4, 1), " +
                "('研发一组', 'DEV1', 2, 1, 1), " +
                "('研发二组', 'DEV2', 2, 2, 1)"
            );
            System.out.println("  - 部门数据已插入");

            // 员工数据
            stmt.execute(
                "INSERT INTO `employee` (`emp_no`, `name`, `gender`, `phone`, `email`, `dept_id`, `position`, `entry_date`, `status`) VALUES " +
                "('E0001', '张三', 1, '13800138001', 'zhangsan@example.com', 2, '技术经理', '2023-01-01', 1), " +
                "('E0002', '李四', 1, '13800138002', 'lisi@example.com', 2, '高级开发', '2023-03-15', 1), " +
                "('E0003', '王五', 1, '13800138003', 'wangwu@example.com', 2, '开发工程师', '2023-06-01', 1), " +
                "('E0004', '赵六', 0, '13800138004', 'zhaoliu@example.com', 3, '人事经理', '2023-02-01', 1), " +
                "('E0005', '钱七', 0, '13800138005', 'qianqi@example.com', 4, '财务经理', '2023-01-15', 1)"
            );
            System.out.println("  - 员工数据已插入");

            // 用户数据（默认密码 123456，使用 BCrypt 加密）
            String bcryptHash = "$2a$10$DPpdT/e7.H9OngvEP4Ep0OlaHKQt81ebtQI93OtQpRqe9bn4IZZa6";
            stmt.execute(
                "INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES " +
                "('admin', '" + bcryptHash + "', '系统管理员', '13800000000', 'admin@example.com', 1), " +
                "('user1', '" + bcryptHash + "', '普通员工', '13800000001', 'user1@example.com', 1), " +
                "('hr1', '" + bcryptHash + "', '人事经理', '13800000002', 'hr1@example.com', 1)"
            );
            System.out.println("  - 用户数据已插入");

            // 角色数据
            stmt.execute(
                "INSERT INTO `role` (`role_name`, `role_code`, `description`, `status`) VALUES " +
                "('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1), " +
                "('人事经理', 'HR_MANAGER', '人事部门经理角色', 1), " +
                "('普通员工', 'EMPLOYEE', '普通员工角色', 1)"
            );
            System.out.println("  - 角色数据已插入");

            // 权限数据
            stmt.execute(
                "INSERT INTO `permission` (`perm_name`, `perm_code`, `perm_type`, `parent_id`, `path`, `icon`, `sort`) VALUES " +
                "('系统管理', 'SYSTEM', 1, 0, '/system', 'setting', 1), " +
                "('员工管理', 'EMPLOYEE', 1, 0, '/employee', 'user', 2), " +
                "('部门管理', 'DEPARTMENT', 1, 0, '/department', 'dept', 3), " +
                "('角色管理', 'ROLE', 1, 0, '/role', 'role', 4), " +
                "('员工列表', 'EMPLOYEE_LIST', 2, 2, '/employee/list', NULL, 1), " +
                "('员工新增', 'EMPLOYEE_ADD', 2, 2, '/employee/add', NULL, 2), " +
                "('员工编辑', 'EMPLOYEE_EDIT', 2, 2, '/employee/edit', NULL, 3), " +
                "('员工删除', 'EMPLOYEE_DELETE', 2, 2, '/employee/delete', NULL, 4), " +
                "('部门列表', 'DEPT_LIST', 2, 3, '/department/list', NULL, 1), " +
                "('部门新增', 'DEPT_ADD', 2, 3, '/department/add', NULL, 2), " +
                "('部门编辑', 'DEPT_EDIT', 2, 3, '/department/edit', NULL, 3), " +
                "('部门删除', 'DEPT_DELETE', 2, 3, '/department/delete', NULL, 4)"
            );
            System.out.println("  - 权限数据已插入");

            // 用户角色关联
            stmt.execute(
                "INSERT INTO `user_role` (`user_id`, `role_id`) VALUES " +
                "(1, 1), " +  // admin 是超级管理员
                "(2, 3), " +  // user1 是普通员工
                "(3, 2)"      // hr1 是人事经理
            );
            System.out.println("  - 用户角色关联已插入");

            // 角色权限关联
            // 超级管理员拥有所有权限
            stmt.execute(
                "INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES " +
                "(1, 1), (1, 2), (1, 3), (1, 4), " +
                "(1, 5), (1, 6), (1, 7), (1, 8), " +
                "(1, 9), (1, 10), (1, 11), (1, 12)"
            );
            // 人事经理权限
            stmt.execute(
                "INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES " +
                "(2, 2), (2, 3), " +
                "(2, 5), (2, 6), (2, 7), " +
                "(2, 9), (2, 10), (2, 11)"
            );
            // 普通员工权限
            stmt.execute(
                "INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES " +
                "(3, 2), (3, 5)"
            );
            System.out.println("  - 角色权限关联已插入");

            // 启用外键检查
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("\n===========================================");
            System.out.println("数据插入完成，正在验证...");
            System.out.println("===========================================\n");

            // 验证数据
            System.out.println("=== 用户数据验证 ===");
            ResultSet rs = stmt.executeQuery("SELECT id, username, password FROM user ORDER BY id");
            int verified = 0;
            while (rs.next()) {
                String password = rs.getString("password");
                boolean matches = PasswordEncoder.matches("123456", password);
                System.out.println("  ID: " + rs.getInt("id") +
                                   ", 用户名：" + rs.getString("username") +
                                   ", 密码验证：" + (matches ? "✓ 匹配" : "✗ 不匹配"));
                if (matches) verified++;
            }

            System.out.println("\n===========================================");
            if (verified == 3) {
                System.out.println("初始化成功！所有用户密码验证通过！");
            } else {
                System.out.println("警告：" + verified + "/3 用户密码验证通过");
            }
            System.out.println("测试账号：");
            System.out.println("  admin / 123456  (超级管理员)");
            System.out.println("  hr1 / 123456    (人事经理/部门负责人)");
            System.out.println("  user1 / 123456  (普通员工)");
            System.out.println("===========================================");

        } catch (Exception e) {
            System.err.println("错误：" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
