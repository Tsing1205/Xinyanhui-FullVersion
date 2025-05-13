package com.example.service;

import com.example.utils.Result;
import org.springframework.stereotype.Service;

@Service
public interface AskforLeaveService {
    Result askforLeave(Integer consultantId, String date, String time, String cancellationReason);
    Result approveLeave(Integer scheduleId, Boolean isApproved);
    Result viewLeave();
}
