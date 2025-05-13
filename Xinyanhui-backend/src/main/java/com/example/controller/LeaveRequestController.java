package com.example.controller;

import com.example.service.AskforLeaveService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class LeaveRequestController {

    @Autowired
    private AskforLeaveService askforLeaveService;

    @PostMapping("/consultant/leave")
    public Result askForLeave(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中提取参数
        Integer consultantId = ((Number) requestBody.get("consultantId")).intValue(); // 转换为Integer
        String date = (String) requestBody.get("date");
        String time = (String) requestBody.get("time");
        String cancellationReason = (String) requestBody.get("cancellationReason");

        // 调用服务层方法
        return askforLeaveService.askforLeave(consultantId, date, time, cancellationReason);
    }

    @PostMapping("/admin/approveLeave")
    public Result approveLeave(@RequestBody Map<String, Object> body) {
        Integer scheduleId = (Integer) body.get("scheduleId");
        Boolean isApproved = (Boolean) body.get("isApproved");

        return askforLeaveService.approveLeave(scheduleId, isApproved);
    }

    @GetMapping("/admin/getLeaveList")
    public Result getLeaveList() {
        return askforLeaveService.viewLeave();
    }
}
