package com.example.interceptor;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class SupervisorCheckInterceptor extends IdentificationCheckInterceptor{

    public SupervisorCheckInterceptor(){
        setType("supervisor");
    }
}
