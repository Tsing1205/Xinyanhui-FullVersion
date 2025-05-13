package com.example.controller;

import com.example.pojo.Consultant;
import com.example.pojo.Supervisor;
import com.example.pojo.ConsultantSchedule;
import com.example.pojo.SupervisorSchedule;
import com.example.service.ViewService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class ListController {

    @Autowired
    private ViewService scheduleService;

    // Consultant查看自己的排班
    @GetMapping("/consultant/schedules")
    public Result<List<ConsultantSchedule>> getConsultantSchedules(@RequestParam Integer consultantId) {
        return scheduleService.getConsultantSchedule(consultantId);
    }

    // Supervisor查看自己的排班
    @GetMapping("/supervisor/schedules")
    public Result<List<SupervisorSchedule>> getSupervisorSchedules(@RequestParam Integer supervisorId) {
        return scheduleService.getSupervisorSchedule(supervisorId);
    }

    // Admin查看所有consultant列表
    @GetMapping("/admin/all-consultants")
    public Result<List<Consultant>> getAllConsultants(@RequestParam(required = false) Integer supervisorId) {
        return scheduleService.getAllConsultants(supervisorId);
    }

    // Admin查看所有supervisor列表
    @GetMapping("/admin/all-supervisors")
    public Result<List<Supervisor>> getAllSupervisors() {
        return scheduleService.getAllSupervisors();
    }
}