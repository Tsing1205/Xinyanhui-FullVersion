package com.example.service;

import com.example.utils.Result;

import java.util.List;
import java.util.Map;

public interface DataService {
    Result<Long> getTotalConsultation();
    Result<List<Map<String,Object>>> getWeeklyConsultation(Integer num);
    Result<List<Map<String,Object>>> getConsultationTimeByMonth();
    Result<List<Map<String,Object>>> abnormalConsultation();
    Result<List<Map<String,Object>>> abnormalSupervise();
}
