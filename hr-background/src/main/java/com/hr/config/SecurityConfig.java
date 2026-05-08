package com.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 禁用默认的安全认证，使用自定义的登录逻辑
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF
            .csrf().disable()
            // 禁用默认认证
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            // 配置请求授权
            .authorizeRequests()
            // 放行所有请求（因为我们使用自己的登录逻辑）
            .anyRequest().permitAll()
            // 禁用 Session 创建
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            // 允许跨域（在 CorsConfig 中已配置）
            .and()
            // 禁用 iframe 的 X-Frame-Options
            .headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
