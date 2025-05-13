package com.example.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pojo.ConSessionStatus;
import com.example.pojo.ConsultationSession;
import com.example.repository.ConsultantDao;
import com.example.repository.ConsultationSessionDao;
import com.example.repository.SupervisorConsultationDao;
import com.example.service.DataService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {
    private final ConsultationSessionDao consultationSessionDao;
    private final ConsultantDao consultantDao;
    private final SupervisorConsultationDao supervisorConsultationDao;

    @Autowired
    public DataServiceImpl(ConsultationSessionDao consultationSessionDao, ConsultantDao consultantDao, SupervisorConsultationDao supervisorConsultationDao) {
        this.consultationSessionDao = consultationSessionDao;
        this.consultantDao = consultantDao;
        this.supervisorConsultationDao = supervisorConsultationDao;
    }

    @Override
    public Result<Long> getTotalConsultation() {
        QueryWrapper<ConsultationSession> query = new QueryWrapper<>();
        query.eq("session_status", ConSessionStatus.COMPLETED)
                .or()
                .eq("session_status", ConSessionStatus.ACTIVE);
        return Result.success(consultationSessionDao.selectCount(query));
    }

    @Override
    public Result<List<Map<String, Object>>> getWeeklyConsultation(Integer num) {
        return Result.success(consultationSessionDao.getWeeklyConsultation(num));
    }

    @Override
    public Result<List<Map<String, Object>>> getConsultationTimeByMonth() {
        List<Map<String, Object>> list = consultantDao.getConsultantWeeklyTime();
        if(list==null){
            return Result.error("无数据");
        }
        return Result.success(list);
    }

    @Override
    public Result<List<Map<String, Object>>> abnormalConsultation() {
        return Result.success(consultationSessionDao.getAbnormalConsultation());
    }

    @Override
    public Result<List<Map<String, Object>>> abnormalSupervise() {
        return Result.success(supervisorConsultationDao.getAbnormalSupervise());
    }
}
