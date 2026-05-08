package com.hr.util;

import java.sql.*;

/**
 * 数据库状态检查工具
 */
public class CheckDb {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            System.out.println("===========================================");
            System.out.println("数据库状态检查");
            System.out.println("===========================================");

            // 检查表是否存在
            DatabaseMetaData meta = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = meta.getTables(null, null, "%", types);

            System.out.println("\n数据库中的表:");
            while (tables.next()) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
            }

            // 检查用户表数据
            System.out.println("\n=== 用户表数据 ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, SUBSTRING(password, 1, 20) AS pw FROM user ORDER BY id");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id") +
                                   ", 用户名：" + rs.getString("username") +
                                   ", 密码前缀：" + rs.getString("pw") + "...");
            }

            // 检查角色表数据
            System.out.println("\n=== 角色表数据 ===");
            rs = stmt.executeQuery("SELECT id, role_name, role_code FROM role ORDER BY id");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id") +
                                   ", 角色名：" + rs.getString("role_name") +
                                   ", 编码：" + rs.getString("role_code"));
            }

            System.out.println("\n===========================================");

        } catch (Exception e) {
            System.err.println("错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
