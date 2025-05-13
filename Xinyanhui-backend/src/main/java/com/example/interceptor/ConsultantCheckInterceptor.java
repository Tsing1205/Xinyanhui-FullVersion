package com.example.interceptor;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class ConsultantCheckInterceptor extends IdentificationCheckInterceptor{

    public ConsultantCheckInterceptor(){
        setType("consultant");
    }
}
