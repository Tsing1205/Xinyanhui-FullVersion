package com.example.controller;

import com.example.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import com.example.utils.Result;

@RestController
@RequestMapping("/internal/admin/data")
public class DataController {
    private final DataService dataService;
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/consultation/week")
    public Result<List<Map<String,Object>>> getWeeklyConsultation(@RequestParam Integer num) {
        return dataService.getWeeklyConsultation(num);
    }

    @GetMapping("/consultation/total")
    public Result<Long> getTotalConsultation() {
        return dataService.getTotalConsultation();
    }

    @GetMapping("/consultant/time")
    public Result<List<Map<String,Object>>> getConsultationTimeByMonth() {
        return dataService.getConsultationTimeByMonth();
    }

    @GetMapping("/consultation/abnormal")
    public Result<List<Map<String,Object>>> abnormalConsultation() {
        return dataService.abnormalConsultation();
    }

    @GetMapping("/supervise/abnormal")
    public Result<List<Map<String,Object>>> abnormalSupervise() {
        return dataService.abnormalSupervise();
    }
}
