package com.example.service.serviceImpl;

import com.example.pojo.*;
import com.example.repository.ConsultantDao;
import com.example.repository.ConsultantSchedulesRepository;
import com.example.repository.SupervisorDao;
import com.example.service.ViewService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewServiceImpl implements ViewService {
    private final ConsultantDao consultantDao;

    @Autowired
    public ViewServiceImpl(ConsultantDao consultantDao) {
        this.consultantDao = consultantDao;
    }

    @Autowired
    private ConsultantSchedulesRepository consultantSchedulesRepository;

    @Autowired
    private SupervisorDao supervisorDao;

    // consultant查看自己的排班
    @Override
    public Result<List<ConsultantSchedule>> getConsultantSchedule(Integer consultantId) {
        return Result.success(consultantSchedulesRepository.getConsultantScheduleById(consultantId));
    }

    // supervisor查看自己的排班
    @Override
    public Result<List<SupervisorSchedule>> getSupervisorSchedule(Integer supervisorId) {
        return Result.success(consultantSchedulesRepository.getSupervisorScheduleById(supervisorId));
    }

    // admin查看所有consultant列表
    @Override
    public Result<List<Consultant>> getAllConsultants(Integer supervisorId) {
        return Result.success(consultantDao.getAllConsultants(supervisorId));
    }

    // admin查看所有supervisor列表
    @Override
    public Result<List<Supervisor>> getAllSupervisors() {
        return Result.success(supervisorDao.getAllSupervisors());
    }

    @Override
    public Result<List<Consultant>> viewConsultantsService(LocalDate date){
        LocalDate today = LocalDate.now();
        if(date==null){
            return Result.success(consultantDao.getScheduledConsultants());
        }
        else if(date.isEqual(today)){
            return Result.success(consultantDao.getAvailableConsultants());
        }
        else{
            return Result.success(consultantDao.getScheduledConsultantsByDate(date));
        }
    }

    @Override
    public Result<Map<LocalDateTime, AvailableStatus>> getAvailableTimeService(Integer Id){
        Map<LocalDateTime, AvailableStatus> availableTime = new LinkedHashMap<>();

        Short status = consultantDao.getAvailableStatus(Id);
        if(status!=null){
            availableTime.put(LocalDateTime.MIN,AvailableStatus.transShort( status ));
        }

        List<MorningAvailableRecord> morningAvailableRecords = consultantDao.getMorningAvailableRecords(Id);
        for(MorningAvailableRecord record:morningAvailableRecords){
            LocalDate date = record.getAvailableDate();
            LocalTime time8 = LocalTime.of(8,0);
            LocalTime time9 = LocalTime.of(9,0);
            LocalTime time10 = LocalTime.of(10,0);
            LocalTime time11 = LocalTime.of(11,0);
            LocalTime time12 = LocalTime.of(12,0);

            availableTime.put(LocalDateTime.of(date,time8), AvailableStatus.transShort(record.getHour8Available()) );
            availableTime.put(LocalDateTime.of(date,time9), AvailableStatus.transShort(record.getHour9Available()) );
            availableTime.put(LocalDateTime.of(date,time10), AvailableStatus.transShort(record.getHour10Available()) );
            availableTime.put(LocalDateTime.of(date,time11), AvailableStatus.transShort(record.getHour11Available()) );
            availableTime.put(LocalDateTime.of(date,time12), AvailableStatus.transShort(record.getHour12Available()) );

        }

        List<AfternoonAvailableRecord> afternoonAvailableRecords = consultantDao.getAfternoonAvailableRecords(Id);
        for(AfternoonAvailableRecord record:afternoonAvailableRecords){
            LocalDate date = record.getAvailableDate();
            LocalTime time13 = LocalTime.of(13,0);
            LocalTime time14 = LocalTime.of(14,0);
            LocalTime time15 = LocalTime.of(15,0);
            LocalTime time16 = LocalTime.of(16,0);
            LocalTime time17 = LocalTime.of(17,0);

            availableTime.put(LocalDateTime.of(date,time13), AvailableStatus.transShort(record.getHour13Available()) );
            availableTime.put(LocalDateTime.of(date,time14), AvailableStatus.transShort(record.getHour14Available()) );
            availableTime.put(LocalDateTime.of(date,time15), AvailableStatus.transShort(record.getHour15Available()) );
            availableTime.put(LocalDateTime.of(date,time16), AvailableStatus.transShort(record.getHour16Available()) );
            availableTime.put(LocalDateTime.of(date,time17), AvailableStatus.transShort(record.getHour17Available()) );

        }
        return Result.success(availableTime);
    }
}
