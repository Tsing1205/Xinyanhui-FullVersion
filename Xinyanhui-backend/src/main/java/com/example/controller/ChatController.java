package com.example.controller;

import com.example.pojo.ChatExportVo;
import com.example.pojo.ChatMsg;
import com.example.pojo.ConsultationSession;
import com.example.pojo.SupervisorConsultation;
import com.example.service.ChatLogService;
import com.example.service.SessionRecordService;
import com.example.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final  SessionRecordService sessionRecordService;
    private final ChatLogService chatLogService;

    @Autowired
    public ChatController(SessionRecordService sessionRecordService, ChatLogService chatLogService) {
        this.sessionRecordService = sessionRecordService;
        this.chatLogService = chatLogService;
    }

    @GetMapping("/user/session")
    public Result<Map<String,Integer>> getSessionIdByUser(@RequestParam Integer appointmentId){
        return sessionRecordService.getSessionId(appointmentId);
    }

    @GetMapping("/internal/consultant/session")
    public Result<Map<String,Integer>> getSessionIdByConsultant(@RequestParam Integer appointmentId){
        return sessionRecordService.getSessionId(appointmentId);
    }

    @PostMapping("/user/session")
    public Result<Map<String,Integer>> newSession(@RequestParam Integer consultantId,
                                                  @RequestParam Integer userId){
        return sessionRecordService.newSession(consultantId,userId);
    }

    @PostMapping("/internal/consultant/supervise")
    public Result<Map<String,Integer>> newRecord(@RequestParam Integer consultantId,
                                                 @RequestParam Integer sessionId){
        return sessionRecordService.newRecord(consultantId,sessionId);
    }

    @GetMapping("/internal/session/history")
    public Result<List<ChatMsg>> getChatHistory(@RequestParam Integer sessionId){
        return chatLogService.getSessionLog(sessionId);
    }

    @GetMapping("/user/session/history")
    public Result<List<ChatMsg>> getChatHistoryByUser(@RequestParam Integer sessionId){
        return chatLogService.getSessionLog(sessionId);
    }

    @GetMapping("/internal/supervise/history")
    public Result<List<ChatMsg>> getSuperviseHistory(@RequestParam Integer recordId){
        return chatLogService.getRecordLog(recordId);
    }

    @PutMapping("/user/session/evaluate")
    public Result<ConsultationSession> evaluate(@RequestBody Map<String,Object> params){
        Integer sessionId = (Integer)params.get("sessionId");
        Integer rate = (Integer)params.get("rate");
        String feedback = (String)params.get("feedback");
        if(sessionId == null || rate == null || feedback == null){
            return Result.error("2","请求参数错误！");
        }
        return sessionRecordService.evaluate(sessionId,  rate, feedback);
    }

    @GetMapping("/user/consultant/evaluation")
    public Result<Map<String,Object>> getEvaluationOfConsultant(@RequestParam Integer consultantId){
        return sessionRecordService.getEvaluationOfConsultant(consultantId);
    }

    @GetMapping("/user/session/list")
    public Result<List<Map<String,Object>>> getSessionsByUserId(@RequestParam Integer userId,
                                                                @RequestParam(required = false) LocalDate startDate ,
                                                                @RequestParam(required = false) LocalDate endDate){
        return sessionRecordService.getSessionsWithNameByUserId(userId, startDate, endDate);
    }

    @GetMapping("/internal/session/list")
    public Result<List<Map<String,Object>>> getSessionsByConsultantId(@RequestParam Integer consultantId,
                                                                      @RequestParam(required = false) LocalDate startDate ,
                                                                      @RequestParam(required = false) LocalDate endDate){
        return sessionRecordService.getSessionsWithNameByConsultantId(consultantId,startDate,endDate);
    }

    @GetMapping("/internal/evaluation")
    public Result<Map<String,Object>> getEvaluationOfConsultantByInternal(@RequestParam Integer consultantId){
        return sessionRecordService.getEvaluationOfConsultant(consultantId);
    }

    @GetMapping("/internal/supervise/list/consultant")
    public Result<List<Map<String,Object>>> getRecordsByConsultantId(@RequestParam Integer consultantId,
                                                                     @RequestParam(required = false) LocalDate startDate,
                                                                     @RequestParam(required = false) LocalDate endDate){
        return sessionRecordService.getRecordsWithNameByConsultantId(consultantId,startDate,endDate);
    }

    @GetMapping("/internal/supervise/list/supervisor")
    public Result<List<Map<String,Object>>> getRecordsBySupervisorId(@RequestParam Integer supervisorId,
                                                                     @RequestParam(required = false) LocalDate startDate,
                                                                     @RequestParam(required = false) LocalDate endDate){
        return sessionRecordService.getRecordsWithNameBySupervisorId(supervisorId,startDate,endDate);
    }

    @GetMapping("/internal/session/export")
    public void exportChatHistory(@RequestParam Integer sessionId, HttpServletResponse response) {
        try {
            Result<List<ChatExportVo>> result = chatLogService.getExportDataBySessionId(sessionId);
            if (!"1".equals(result.getCode())) {
                response.setContentType("text/plain;charset=utf-8");
                response.getWriter().write("导出失败: " + result.getMsg());
                return;
            }

            List<ChatExportVo> chatList = result.getData();

            StringBuilder sb = new StringBuilder();
            sb.append("聊天记录导出\n\n");
            for (ChatExportVo chat : chatList) {
                sb.append("发送方: ").append(chat.getSender()).append("\n");
                sb.append("接收方: ").append(chat.getReceiver()).append("\n");
                sb.append("内容: ").append(chat.getMessage()).append("\n");
                sb.append("时间: ").append(chat.getTimestamp()).append("\n\n");
            }

            // 设置响应头为 txt 文件下载
            response.setContentType("text/plain;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=chat_session_" + sessionId + ".txt");

            // 写入内容
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("text/plain;charset=utf-8");
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (IOException ignored) {}
        }
    }
}
