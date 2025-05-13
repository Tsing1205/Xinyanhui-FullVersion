package com.example.controller;

import com.example.pojo.Admin;
import com.example.pojo.Consultant;
import com.example.pojo.Supervisor;
import com.example.service.AdminService;
import com.example.service.ConsultantService;
import com.example.service.SupervisorService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/login")
public class InternalLoginController {
    private final ConsultantService consultantService;
    private final SupervisorService supervisorService;
    private final AdminService adminService;

    @Autowired
    public InternalLoginController(ConsultantService consultantService,
                                   SupervisorService supervisorService,AdminService adminService) {
        this.consultantService = consultantService;
        this.supervisorService = supervisorService;
        this.adminService = adminService;
    }

    @PostMapping("/consultant")
    public Result<Consultant> loginAsConsultant(@RequestParam Integer id, @RequestParam String password) {
        if(id==null || password==null || password.length()==0){
            return Result.error("参数错误");
        }
        return consultantService.loginService(id, password);
    }

    @PostMapping("/supervisor")
    public Result<Supervisor> loginAsSupervisor(@RequestParam Integer id, @RequestParam String password) {
        if(id==null || password==null || password.length()==0){
            return Result.error("参数错误");
        }
        return supervisorService.loginService(id, password);
    }

    @PostMapping("/admin")
    public Result<Admin> loginAsAdmin(@RequestParam Integer id, @RequestParam String password) {
        if(id==null || password==null || password.length()==0){
            return Result.error("参数错误");
        }
        return adminService.loginService(id, password);
    }
}
