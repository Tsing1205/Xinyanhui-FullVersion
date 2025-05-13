package com.example.interceptor;

import ch.qos.logback.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.example.utils.JwtUtil;
import com.example.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@Order(1)
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        String token = request.getHeader("token");  //get jwt token from request header
        log.info("token:{}",token);

        if(!StringUtils.hasLength(token)){
            log.info("token is empty");

            Result responseResult = Result.error("未登录");
            String json = JSON.toJSONString(responseResult);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            return false;
        }

        try{
            JwtUtil.parseJwt(token);
        } catch (Exception e) {
            log.info("parse jwt fail");
            Result responseResult = Result.error("未登录");
            String json = JSON.toJSONString(responseResult);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            return false;
        }
        return true;
    }
}
