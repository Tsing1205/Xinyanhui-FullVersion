package com.example.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.constants.TypeConstant;
import com.example.pojo.ConSessionStatus;
import com.example.pojo.ConsultationSession;
import com.example.pojo.NotifyMsg;
import com.example.pojo.SupervisorConsultation;
import com.example.repository.*;
import com.example.service.NotifyService;
import com.example.service.SessionRecordService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionRecordServiceImpl implements SessionRecordService {
    private final ConsultationSessionDao  consultationSessionDao;
    private final SupervisorConsultationDao supervisorConsultationDao;
    private final ConsultantDao consultantDao;
    private final NotifyService notifyService;
  //  private final UserDao userDao;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public SessionRecordServiceImpl(ConsultationSessionDao consultationSessionDao, SupervisorConsultationDao supervisorConsultationDao,
                                 ConsultantDao consultantDao,  NotifyService notifyService,
                                AppointmentRepository appointmentRepository/*, UserDao userDao */) {
        this.consultationSessionDao = consultationSessionDao;
        this.supervisorConsultationDao = supervisorConsultationDao;
        this.consultantDao = consultantDao;
        this.notifyService = notifyService;
        this.appointmentRepository = appointmentRepository;
       // this.userDao = userDao;
    }

    @Override
    public Result<Map<String, Integer>> getSessionId(int apointmentId) {
        Integer id = consultationSessionDao.findIdByAppointmentId(apointmentId);
        return id == null ? Result.error("未找到会话") : Result.success(Map.of("sessionId", id));
    }

    @Override
    @Transactional
    public Result<Map<String, Integer>> newSession(int consultantId, int userId) {
        //检查两小时内用户是否有会话
        QueryWrapper<ConsultationSession> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .ge("start_time", LocalDateTime.now().minusHours(2));
        if (consultationSessionDao.selectCount(query) > 0) {
            return Result.error("两小时内已存在会话,当前无法开启新会话");
        }
        //检查用户三天内是否有预约
        int count =appointmentRepository.countAppointmentsByDateRange(userId,LocalDate.now().minusDays(3),LocalDate.now().plusDays(3));
        if(count>0){
            return Result.error("三天内已有预约或进行过咨询,当前无法开启新会话");
        }
        //封装会话信息，并存入数据库
        ConsultationSession cs = new ConsultationSession();
        cs.setUserId(userId);
        cs.setStartTime(LocalDateTime.now());
        cs.setSessionStatus(ConSessionStatus.READY);
        cs.setConsultantId(consultantId);
        if (consultationSessionDao.insert(cs) == 1) {
            Map<String, Integer> map = new HashMap<>();
            map.put("sessionId", cs.getSessionId());
            //通知咨询师
            NotifyMsg msg = NotifyMsg.getSessionMsg(map);
            notifyService.sendMessage(consultantId, TypeConstant.CONSULTANT_INT, msg);
            // 封装返回数据
            map.put("userId",  cs.getUserId());
            map.put("consultantId", cs.getConsultantId());
            return Result.success(map);
        }
        return Result.error("创建会话失败");
    }

    @Override
    @Transactional
    public Result<Map<String, Integer>> newRecord(int consultantId,  int sessionId) {
        SupervisorConsultation sc = new SupervisorConsultation();
        sc.setConsultantId(consultantId);
        Integer supervisorId = consultantDao.findSupervisorIdById(consultantId);
        sc.setSupervisorId(supervisorId);
        sc.setSessionId(sessionId);
        sc.setRequestTime(LocalDateTime.now());
        if (supervisorConsultationDao.insert(sc) == 1) {
            Map<String, Integer> map = new HashMap<>();
            //封装notify数据
            map.put("sessionId", sc.getRecordId());
            map.put("observeSessionId", sc.getSessionId());
            //通知督导
            NotifyMsg msg = NotifyMsg.getSessionMsg(map);
            notifyService.sendMessage(sc.getSupervisorId(), TypeConstant.SUPERVISOR_INT, msg);
            map.remove("observeSessionId");
            // 封装返回数据
            map.put("recordId", sc.getRecordId());
            map.put("sessionId", sc.getSessionId());
            map.put("consultantId", sc.getConsultantId());
            map.put("supervisorId", sc.getSupervisorId());
            return Result.success(map);
        }
        return Result.error("创建督导会话失败");
    }

    @Override
    public Result<ConsultationSession> evaluate(int sessionId, int rating, String feedback) {
        ConsultationSession cs = new ConsultationSession();
        cs.setSessionId(sessionId);
        cs.setRating(rating);
        cs.setFeedback(feedback);
        if(consultationSessionDao.updateById(cs)==1){
            return Result.success(cs);
        }
        return Result.error("评价失败");
    }

    @Override
    public Result<List<ConsultationSession>> getSessionsByConsultantId(int consultantId) {
        QueryWrapper<ConsultationSession> query = new QueryWrapper<>();
        query.eq("consultant_id", consultantId).eq("session_status", ConSessionStatus.COMPLETED);
        List<ConsultationSession> list = consultationSessionDao.selectList(query);
        if(list==null){
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<ConsultationSession>> getSessionsByUserId(int userId) {
        QueryWrapper<ConsultationSession> query = new QueryWrapper<>();
        query.eq("user_id", userId).eq("session_status", ConSessionStatus.COMPLETED);
        List<ConsultationSession> list = consultationSessionDao.selectList(query);
        if(list==null){
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }


    @Override
    public Result<Map<String, Object>> getEvaluationOfConsultant(int consultantId) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<ConsultationSession> query = new QueryWrapper<>();
        query.eq("consultant_id", consultantId)
                .eq("session_status", ConSessionStatus.COMPLETED)
                .isNotNull("rating");
        List<ConsultationSession> list = consultationSessionDao.selectList(query);
        if (list == null) {
            return Result.error("未找到会话");
        }
        int averageRating = 0;
        if(!list.isEmpty()){
            averageRating = list.stream().mapToInt(ConsultationSession::getRating).sum() / list.size();
        }
        map.put("averageRate", averageRating);
        map.put("sessionList", list);
        return Result.success(map);
    }

    @Override
    public Result<List<SupervisorConsultation>> getRecordsByConsultantId(int consultantId) {
        QueryWrapper<SupervisorConsultation> query = new QueryWrapper<>();
        query.eq("consultant_id", consultantId);
        List<SupervisorConsultation> list = supervisorConsultationDao.selectList(query);
        if (list == null) {
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<SupervisorConsultation>> getRecordsBySupervisorId(int supervisorId) {
        QueryWrapper<SupervisorConsultation> query = new QueryWrapper<>();
        query.eq("supervisor_id", supervisorId);
        List<SupervisorConsultation> list = supervisorConsultationDao.selectList(query);
        if (list == null) {
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<Map<String, Object>>> getSessionsWithNameByConsultantId(int consultantId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime= startDate==null ? null : startDate.atStartOfDay();
        LocalDateTime endTime= endDate==null ? null : endDate.atTime(23,59,59);
        List<Map<String,Object>> list =consultationSessionDao.getSessionWithNameByConsultantId(consultantId, startTime, endTime);
        if(list==null){
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<Map<String, Object>>> getSessionsWithNameByUserId(int userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime= startDate==null ? null : startDate.atStartOfDay();
        LocalDateTime endTime= endDate==null ? null : endDate.atTime(23,59,59);
        List<Map<String, Object>> list = consultationSessionDao.getSessionWithNameByUserId(userId, startTime, endTime);
        if(list==null){
            return Result.error("未找到会话");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<Map<String, Object>>> getRecordsWithNameByConsultantId(int consultantId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime= startDate==null ? null : startDate.atStartOfDay();
        LocalDateTime endTime= endDate==null ? null : endDate.atTime(23,59,59);
        List<Map<String, Object>> list = supervisorConsultationDao.findWithNameByConsultantId(consultantId, startTime, endTime);
        if(list==null){
            return Result.error("无数据");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<Map<String, Object>>> getRecordsWithNameBySupervisorId(int supervisorId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime= startDate==null ? null : startDate.atStartOfDay();
        LocalDateTime endTime= endDate==null ? null : endDate.atTime(23,59,59);
        List<Map<String, Object>> list = supervisorConsultationDao.findWithNameBySupervisorId(supervisorId, startTime, endTime);
        if(list==null){
            return Result.error("无数据");
        }
        return Result.success(list);
    }


}
