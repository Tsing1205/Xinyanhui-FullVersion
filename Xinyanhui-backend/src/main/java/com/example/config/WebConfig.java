package com.example.config;

import com.example.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final UserCheckInterceptor userCheckInterceptor;
    private final AdminCheckInterceptor adminCheckInterceptor;
    private final ConsultantCheckInterceptor consultantCheckInterceptor;
    private final SupervisorCheckInterceptor supervisorCheckInterceptor;
    private final InternalCheckInterceptor internalCheckInterceptor;

    @Autowired
    public WebConfig(LoginCheckInterceptor login, UserCheckInterceptor user, AdminCheckInterceptor admin,
                     ConsultantCheckInterceptor consultant,SupervisorCheckInterceptor supervisor,
                     InternalCheckInterceptor internal) {
        this.loginCheckInterceptor = login;
        this.userCheckInterceptor = user;
        this.adminCheckInterceptor = admin;
        this.consultantCheckInterceptor = consultant;
        this.supervisorCheckInterceptor = supervisor;
        this.internalCheckInterceptor = internal;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/register","/internal/login/*","/chat/**","/notify/**","/observe/**");

        registry.addInterceptor(userCheckInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login","/user/register","/chat/**","/notify/**","/observe/**");

        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/internal/admin/*");

        registry.addInterceptor(consultantCheckInterceptor)
                .addPathPatterns("/internal/consultant/*");

        registry.addInterceptor(supervisorCheckInterceptor)
                .addPathPatterns("/internal/supervisor/*");

        registry.addInterceptor(internalCheckInterceptor)
                .addPathPatterns("/internal/**")
                .excludePathPatterns("/internal/login/*","/chat/**","/notify/**","/observe/**");
    }
}
