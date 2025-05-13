package com.example.controller;

import com.example.pojo.Consultant;
import com.example.pojo.Supervisor;
import com.example.service.AdminService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/internal/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/consultant")
    public Result<Consultant> addConsultant(@RequestParam(name="username") String name,
                                @RequestParam(required = false) String professionalInfo,
                                @RequestParam String password, @RequestParam Integer supervisorId) {
        if(name==null || password==null || name.isEmpty() || password.isEmpty() || supervisorId==null){
            return Result.error("参数错误");
        }
        Consultant consultant = new Consultant();
        consultant.setName(name);
        consultant.setProfessionalInfo(professionalInfo);
        consultant.setPassword(password);
        consultant.setSupervisorId(supervisorId);
        return adminService.addConsultantService(consultant);
    }

    @PostMapping("/supervisor")
    public Result<Supervisor> addSupervisor(@RequestParam(name="username") String name,
                                            @RequestParam(required = false) String professionalInfo,
                                            @RequestParam String password) {
        if(name==null || password==null || name.isEmpty() || password.isEmpty()){
            return Result.error("参数错误");
        }
        Supervisor supervisor = new Supervisor();
        supervisor.setName(name);
        supervisor.setProfessionalInfo(professionalInfo);
        supervisor.setPassword(password);
        return adminService.addSupervisorService(supervisor);
    }

    @PostMapping("/schedule/consultant")
    public Result<List<Map<String,Object>>> manageConsultantSchedule(@RequestBody Map<String,Object> body) {
        if(!body.containsKey("schedule")){
            return Result.error("参数错误");
        }

        List<Map<String,Object>> schedule = (List<Map<String,Object>>) body.get("schedule");
        if(schedule==null || schedule.isEmpty()){
            return Result.error("参数错误");
        }
        return adminService.manageConsultantSchedule(schedule);
    }

    @PostMapping("/schedule/supervisor")
    public Result<List<Map<String,Object>>> manageSupervisorSchedule(@RequestBody Map<String,Object> body) {
        if(!body.containsKey("schedule")){
            return Result.error("参数错误");
        }

        List<Map<String,Object>> schedule = (List<Map<String,Object>>) body.get("schedule");
        if(schedule==null || schedule.isEmpty()){
            return Result.error("参数错误");
        }
        return adminService.manageSupervisorSchedule(schedule);
    }

    @DeleteMapping("/consultant")
    public Result<Consultant> deleteConsultant(@RequestParam Integer consultantId) {
        if(consultantId==null){
            return Result.error("参数错误");
        }
        return adminService.deleteConsultant(consultantId);
    }

    @DeleteMapping("/supervisor")
    public Result<Supervisor> deleteSupervisor(@RequestParam Integer supervisorId) {
        if(supervisorId==null){
            return Result.error("参数错误");
        }
        return adminService.deleteSupervisor(supervisorId);
    }

    @GetMapping("/schedule/consultant")
    public Result<Set<String>> getConsultantNextSchedule(@RequestParam String day, @RequestParam String time) {
        if(day==null || time==null || day.isEmpty() || time.isEmpty()){
            return Result.error("参数错误");
        }
        return adminService.getConsultantNextSchedule(day,time);
    }

    @GetMapping("/schedule/supervisor")
    public Result<Set<String>> getSupervisorNextSchedule(@RequestParam String day, @RequestParam String time) {
        if(day==null || time==null || day.isEmpty() || time.isEmpty()){
            return Result.error("参数错误");
        }
        return adminService.getSupervisorNextSchedule(day,time);
    }

    @DeleteMapping("/schedule/consultant")
    public Result<Map<String,Object>> deleteConsultantSchedule(@RequestBody Map<String,Object> map) {
        return adminService.deleteConsultantSchedule(map);
    }

    @DeleteMapping("/schedule/supervisor")
    public Result<Map<String,Object>> deleteSupervisorSchedule(@RequestBody Map<String,Object> map) {
        return adminService.deleteSupervisorSchedule(map);
    }

}
