package com.hr.util;

import java.sql.*;

/**
 * 数据库初始化工具
 * 用于执行 schema.sql 初始化数据库
 */
public class DatabaseInitializer {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("数据库初始化工具");
        System.out.println("===========================================");
        System.out.println("数据库：" + URL);
        System.out.println("用户名：" + USERNAME);
        System.out.println("===========================================\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // 检查数据库连接
            System.out.println("正在连接数据库...");
            if (conn.isValid(5)) {
                System.out.println("数据库连接成功！\n");
            }

            // 读取并执行 schema.sql
            System.out.println("正在执行 schema.sql...");
            String sql = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("src/main/resources/schema.sql")), "UTF-8");

            // 分割 SQL 语句（按分号分割）
            String[] statements = sql.split(";");
            int executed = 0;

            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    try {
                        stmt.execute(trimmed);
                        executed++;
                    } catch (SQLException e) {
                        // 忽略已存在的表错误
                        if (!e.getMessage().contains("already exists")) {
                            System.out.println("警告：" + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("执行完成！共执行 " + executed + " 条 SQL 语句。\n");

            // 验证数据
            System.out.println("=== 用户数据 ===");
            ResultSet rs = stmt.executeQuery("SELECT id, username, real_name FROM user");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getLong("id") +
                                   ", 用户名：" + rs.getString("username") +
                                   ", 姓名：" + rs.getString("real_name"));
            }

            System.out.println("\n=== 角色数据 ===");
            rs = stmt.executeQuery("SELECT id, role_name, role_code FROM role");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getLong("id") +
                                   ", 角色名：" + rs.getString("role_name") +
                                   ", 编码：" + rs.getString("role_code"));
            }

            System.out.println("\n=== 用户角色关联 ===");
            rs = stmt.executeQuery(
                "SELECT u.username, r.role_name, r.role_code " +
                "FROM user u " +
                "JOIN user_role ur ON u.id = ur.user_id " +
                "JOIN role r ON ur.role_id = r.id");
            while (rs.next()) {
                System.out.println("  " + rs.getString("username") + " -> " +
                                   rs.getString("role_name") + " (" +
                                   rs.getString("role_code") + ")");
            }

            System.out.println("\n===========================================");
            System.out.println("初始化成功！");
            System.out.println("===========================================");
            System.out.println("\n测试账号：");
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
