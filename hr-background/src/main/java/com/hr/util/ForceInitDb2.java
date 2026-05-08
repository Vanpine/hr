package com.hr.util;

import java.sql.*;

/**
 * 强制初始化数据库（删除已有数据后重新插入）
 */
public class ForceInitDb2 {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("强制初始化数据库 - 清空数据后重新插入");
        System.out.println("===========================================\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // 删除所有数据（外键约束需要先删除子表）
            System.out.println("正在删除旧数据...");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("TRUNCATE TABLE hr_system.role_permission");
            stmt.execute("TRUNCATE TABLE hr_system.user_role");
            stmt.execute("TRUNCATE TABLE hr_system.permission");
            stmt.execute("TRUNCATE TABLE hr_system.role");
            stmt.execute("TRUNCATE TABLE hr_system.user");
            stmt.execute("TRUNCATE TABLE hr_system.employee");
            stmt.execute("TRUNCATE TABLE hr_system.department");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("旧数据已删除。\n");

            // 插入部门数据
            System.out.println("正在插入部门数据...");
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

            // 插入员工数据
            System.out.println("正在插入员工数据...");
            stmt.execute(
                "INSERT INTO `employee` (`emp_no`, `name`, `gender`, `phone`, `email`, `dept_id`, `position`, `entry_date`, `status`) VALUES " +
                "('E0001', '张三', 1, '13800138001', 'zhangsan@example.com', 2, '技术经理', '2023-01-01', 1), " +
                "('E0002', '李四', 1, '13800138002', 'lisi@example.com', 2, '高级开发', '2023-03-15', 1), " +
                "('E0003', '王五', 1, '13800138003', 'wangwu@example.com', 2, '开发工程师', '2023-06-01', 1), " +
                "('E0004', '赵六', 0, '13800138004', 'zhaoliu@example.com', 3, '人事经理', '2023-02-01', 1), " +
                "('E0005', '钱七', 0, '13800138005', 'qianqi@example.com', 4, '财务经理', '2023-01-15', 1)"
            );

            // 用户数据（默认密码 123456，使用 BCrypt 加密）
            String bcryptHash = "$2a$10$DPpdT/e7.H9OngvEP4Ep0OlaHKQt81ebtQI93OtQpRqe9bn4IZZa6";
            System.out.println("正在插入用户数据...（密码：" + bcryptHash.substring(0, 15) + "...）");
            stmt.execute(
                "INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES " +
                "('admin', '" + bcryptHash + "', '系统管理员', '13800000000', 'admin@example.com', 1), " +
                "('user1', '" + bcryptHash + "', '普通员工', '13800000001', 'user1@example.com', 1), " +
                "('hr1', '" + bcryptHash + "', '人事经理', '13800000002', 'hr1@example.com', 1)"
            );

            // 插入角色数据
            System.out.println("正在插入角色数据...");
            stmt.execute(
                "INSERT INTO `role` (`role_name`, `role_code`, `description`, `status`) VALUES " +
                "('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1), " +
                "('人事经理', 'HR_MANAGER', '人事部门经理角色', 1), " +
                "('普通员工', 'EMPLOYEE', '普通员工角色', 1)"
            );

            // 插入权限数据
            System.out.println("正在插入权限数据...");
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

            // 插入用户角色关联
            System.out.println("正在插入用户角色关联...");
            stmt.execute(
                "INSERT INTO `user_role` (`user_id`, `role_id`) VALUES " +
                "(1, 1), " +  // admin 是超级管理员
                "(2, 3), " +  // user1 是普通员工
                "(3, 2)"      // hr1 是人事经理
            );

            // 插入角色权限关联
            System.out.println("正在插入角色权限关联...");
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

            System.out.println("\n数据插入完成！\n");

            // 验证数据
            System.out.println("=== 用户数据验证 ===");
            ResultSet rs = stmt.executeQuery("SELECT id, username, password FROM user ORDER BY id");
            while (rs.next()) {
                String password = rs.getString("password");
                boolean matches = PasswordEncoder.matches("123456", password);
                System.out.println("  ID: " + rs.getInt("id") +
                                   ", 用户名：" + rs.getString("username") +
                                   ", 密码验证：" + (matches ? "✓ 匹配" : "✗ 不匹配"));
            }

            System.out.println("\n===========================================");
            System.out.println("初始化成功！");
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
