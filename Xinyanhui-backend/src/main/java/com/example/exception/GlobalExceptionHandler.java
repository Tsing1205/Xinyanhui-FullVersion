package com.example.exception;

import com.example.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e){
        log.error("异常:{}",e.getMessage());
        return Result.error("未知异常");
    }

    @ExceptionHandler(SQLException.class)
    public Result sqlExceptionHandler(SQLException e) throws SQLException {
        log.error("数据库异常");
        throw e;
    }

    @ExceptionHandler(IOException.class)
    public Result ioExceptionHandler(IOException e){
        log.error("异常:{}",e.getMessage());
        return Result.error("读取数据异常");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public Result servletRequestBindingExceptionHandler(ServletRequestBindingException e){
        log.error("异常:{}",e.getMessage());
        return Result.error("请求参数异常");
    }

    @ExceptionHandler(TypeMismatchException.class)
    public Result typeMismatchExceptionHandler(TypeMismatchException e){
        log.error("异常:{}",e.getMessage());
        return Result.error("参数类型错误");
    }
}
