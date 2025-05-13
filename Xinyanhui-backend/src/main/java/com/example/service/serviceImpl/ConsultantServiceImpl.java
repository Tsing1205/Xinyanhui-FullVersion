package com.example.service.serviceImpl;

import com.example.pojo.Consultant;
import com.example.repository.ConsultantDao;
import com.example.service.ConsultantService;
import com.example.utils.JwtUtil;
import com.example.utils.PasswordHashWithSalt;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class ConsultantServiceImpl implements ConsultantService {
    @Autowired
    private ConsultantDao consultantDao;
    @Override
    public Result<Consultant> loginService(Integer Id, String password) {
        String salt = consultantDao.getSaltById(Id);
        if(salt==null){                                  //查不到盐值，即用户不在数据表中
            return Result.error("2","账号不存在");
        }

        try{
            String hashPassword = PasswordHashWithSalt.hashPassword(password, salt);    //根据盐值和输入的密码进行哈希
            Consultant consultant = consultantDao.getByIdAndPassword(Id, hashPassword);  //根据账号id和哈希后的密码查询咨询师
            if(consultant==null){
                return Result.error("2","密码错误");
            }
            Map<String,Object> claims = new HashMap<>();
            claims.put("type","consultant");
            claims.put("id",consultant.getConsultantId());
            consultant.setToken(JwtUtil.generateJwt(claims));
            return Result.success(consultant);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("未知异常");
        }

    }
}
