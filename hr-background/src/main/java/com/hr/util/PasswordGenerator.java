package com.hr.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密码生成工具
 * 用于生成 BCrypt 加密的密码哈希
 *
 * 使用方法：
 * 1. 运行 main 方法，传入明文密码
 * 2. 复制输出的哈希值到数据库或代码中
 *
 * 示例：
 * 明文密码：123456
 * BCrypt 哈希：$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
 */
public class PasswordGenerator {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // strength=10

    /**
     * 生成 BCrypt 密码哈希
     * @param rawPassword 明文密码
     * @return BCrypt 哈希
     */
    public static String generate(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     * @param rawPassword 明文密码
     * @param encodedPassword BCrypt 哈希
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        // 默认生成 123456 的哈希
        String password = args.length > 0 ? args[0] : "123456";
        String hash = generate(password);

        System.out.println("===========================================");
        System.out.println("明文密码：" + password);
        System.out.println("BCrypt 哈希：" + hash);
        System.out.println("===========================================");
        System.out.println("SQL 插入语句：");
        System.out.println("INSERT INTO `user` (`username`, `password`, ...) VALUES ('username', '" + hash + "', ...);");
        System.out.println("===========================================");
    }
}
