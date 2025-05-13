package com.example.service.serviceImpl;

import com.example.pojo.User;
import com.example.repository.UserDao;
import com.example.service.UserService;
import com.example.utils.PasswordHashWithSalt;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User loginService(Integer type, String s, String password) {
        User user = null;
        if(type == 0){//username
            user = userDao.findByUsername(s);
            try{
                String salt = user.getSalt();
                password = PasswordHashWithSalt.hashPassword(password, salt);
            } catch (Exception e) {
                return null;
            }
            user = userDao.findByUsernameAndPassword(s, password);
        }
        if(type == 1) {//phone
            user = userDao.findByPhone(s);
            try{
                String salt = user.getSalt();
                password = PasswordHashWithSalt.hashPassword(password, salt);
            } catch (Exception e) {
                return null;
            }
            user = userDao.findByPhoneAndPassword(s, password);
        }
        if(type == 2){//email
            user = userDao.findByEmail(s);
            try{
                String salt = user.getSalt();
                password = PasswordHashWithSalt.hashPassword(password, salt);
            } catch (Exception e) {
                return null;
            }
            user = userDao.findByEmailAndPassword(s, password);
        }
        // 重要信息置空
        if (user != null) {
            user.setPassword("");
        }
        return user;
    }

    @Override
    public User loginService(String uname, String password) {
        return null;
    }

    @Override
    public User registerService (User user){
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            // 用户名为空，返回null或抛出自定义异常
            return null;
        }
        //当新用户的用户名已存在时
        if(userDao.findByUsername(user.getUsername())!=null){
            // 无法注册
            return null;
        }else{
            // 设置注册日期为当前时间
            user.setRegister_date(new Date());

            // 生成随机值
            user.setSalt(PasswordHashWithSalt.generateSalt());
            // 对密码进行哈希处理
            try{
                String hashedPassword = PasswordHashWithSalt.hashPassword(user.getPassword(), user.getSalt());
                user.setPassword(hashedPassword);
                // 保存用户并返回创建好的用户对象(带uid)
                userDao.save(user);
                return user;
            } catch (Exception e) {
                return null;
            }

        }
    }
}
