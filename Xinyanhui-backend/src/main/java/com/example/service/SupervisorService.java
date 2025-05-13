package com.example.service;

import com.example.pojo.Supervisor;
import com.example.utils.Result;

public interface SupervisorService {
    Result<Supervisor> loginService(Integer Id, String password);
}
