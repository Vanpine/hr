package com.hr.util;

import java.sql.*;

/**
 * 强制初始化数据库
 */
public class ForceInitDb {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("强制初始化数据库");
        System.out.println("===========================================\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // 读取 SQL 文件
            System.out.println("正在读取 force_init_db.sql...");
            String sql = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("src/main/resources/force_init_db.sql")), "UTF-8");

            // 分割并执行 SQL 语句
            String[] statements = sql.split(";");
            int executed = 0;

            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    try {
                        stmt.execute(trimmed);
                        executed++;
                    } catch (SQLException e) {
                        System.out.println("执行 SQL 时警告：" + e.getMessage().substring(0, Math.min(100, e.getMessage().length())));
                    }
                }
            }

            System.out.println("执行完成！共执行 " + executed + " 条 SQL 语句。\n");

            // 验证数据
            System.out.println("=== 用户数据 ===");
            ResultSet rs = stmt.executeQuery("SELECT id, username, password FROM user ORDER BY id");
            while (rs.next()) {
                String password = rs.getString("password");
                boolean matches = PasswordEncoder.matches("123456", password);
                System.out.println("  ID: " + rs.getInt("id") +
                                   ", 用户名：" + rs.getString("username") +
                                   ", 密码验证：" + (matches ? "✓ 匹配" : "✗ 不匹配"));
            }

            System.out.println("\n===========================================");
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
