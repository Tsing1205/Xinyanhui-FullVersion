package com.example.service;

import com.example.pojo.Consultant;
import com.example.utils.Result;

public interface ConsultantService {
    Result<Consultant> loginService(Integer Id, String password);
}
