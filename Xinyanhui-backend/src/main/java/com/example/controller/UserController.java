package com.example.controller;

import com.example.pojo.AvailableStatus;
import com.example.pojo.Consultant;
import com.example.service.ViewService;
import com.example.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;
import com.example.utils.Result;
import com.example.pojo.User;
import com.example.service.UserService;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private ViewService viewService;

    @PostMapping("/login")
    public Result<User> loginController(@RequestBody Map<String, Object> params) {
        // 解析参数
        Integer type = (Integer) params.get("type");
        String account = (String) params.get("account");
        String password = (String) params.get("password");

        // 参数校验
        if (type == null || account == null || password == null) {
            return Result.error("2", "请求参数错误！");
        }

        User user = userService.loginService(type, account, password);
        if (user != null) {
            Map<String,Object> claims = new HashMap<>();
            claims.put("type","user");
            claims.put("id",user.getUserId());
            user.setToken(JwtUtil.generateJwt(claims));
            return Result.success(user, "登录成功！");
        } else {
            return Result.error("2", "账号或密码错误！");
        }
    }

    @PostMapping("/register")
    public Result<User> registerController(@RequestBody User newUser) {
        User user = userService.registerService(newUser);
        if (user != null) {
            return Result.success(user, "注册成功！");
        } else {
            return Result.error( "注册失败！");
        }
    }

    @GetMapping("/consultantlist")
    public Result<List<Consultant>> viewConsultantList(@RequestParam(required = false) LocalDate availableDate){
        return viewService.viewConsultantsService(availableDate);
    }

    @GetMapping("/consultant")
    public Result<Map<LocalDateTime, AvailableStatus>> getAvailableTime(@RequestParam Integer consultantID){
        if (consultantID == null) {
            return Result.error("请求参数错误！");
        }
        return viewService.getAvailableTimeService(consultantID);
    }
}
