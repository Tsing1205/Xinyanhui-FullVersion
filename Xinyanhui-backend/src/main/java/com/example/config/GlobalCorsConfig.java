package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 关闭 CSRF，防止拦截 POST 请求
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 允许跨域
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**", "/appointments/**","/internal/**","/chat/**","/notify/**","/observe/**").permitAll() // 允许访问预约相关接口
                        .anyRequest().authenticated() // 其他接口需要认证
                );

        return http.build();
    }

    // CORS 配置，允许所有请求来源
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8081"); // 允许前端请求来源
        configuration.addAllowedOrigin("http://localhost:8082");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://118.178.178.75");
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的请求方法
        configuration.setAllowedHeaders(List.of("*")); // 允许的请求头
        configuration.setAllowCredentials(true); // 允许携带 cookie 进行跨域请求

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 作用于所有 API
        return source;
    }
}
