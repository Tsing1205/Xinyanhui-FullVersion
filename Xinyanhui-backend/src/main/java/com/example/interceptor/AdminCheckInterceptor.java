package com.example.interceptor;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
public class AdminCheckInterceptor extends IdentificationCheckInterceptor{
    public AdminCheckInterceptor(){
        setType("admin");
    }
}
