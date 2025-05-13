package com.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@Order(3)
public class UserCheckInterceptor extends IdentificationCheckInterceptor {

    public UserCheckInterceptor(){
        setType("user");
    }
}
