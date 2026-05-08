package com.hr.util;

import java.sql.*;

/**
 * 密码验证测试工具
 */
public class PasswordTest {

    private static final String URL = "jdbc:mysql://120.26.30.114:3306/hr_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wangbing201708";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            // 获取数据库中的密码哈希
            PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM user");
            ResultSet rs = ps.executeQuery();

            System.out.println("===========================================");
            System.out.println("密码验证测试");
            System.out.println("===========================================");

            while (rs.next()) {
                String username = rs.getString("username");
                String dbPassword = rs.getString("password");

                boolean matches = PasswordEncoder.matches("123456", dbPassword);

                System.out.println("用户：" + username);
                System.out.println("数据库密码哈希：" + dbPassword);
                System.out.println("验证结果：" + (matches ? "✓ 匹配" : "✗ 不匹配"));
                System.out.println("-------------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
