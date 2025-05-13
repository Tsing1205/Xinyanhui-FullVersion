package com.example.service;

import com.example.pojo.User;

public interface UserService {
    User loginService(Integer type, String s, String password);
    User loginService(String uname, String password);
    User registerService(User user);
}
