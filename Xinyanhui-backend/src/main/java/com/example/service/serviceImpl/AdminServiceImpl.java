package com.example.service.serviceImpl;

import com.example.pojo.*;
import com.example.repository.AdminDao;
import com.example.repository.ConsultantDao;
import com.example.repository.ConsultantSchedulesRepository;
import com.example.repository.SupervisorDao;
import com.example.service.AdminService;
import com.example.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;
    private final ConsultantDao consultantDao;
    private final SupervisorDao supervisorDao;
    private final ConsultantSchedulesRepository consultantSchedulesRepository;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, ConsultantDao consultantDao, SupervisorDao supervisorDao,
                            ConsultantSchedulesRepository consultantSchedulesRepository) {
        this.adminDao = adminDao;
        this.consultantDao = consultantDao;
        this.supervisorDao = supervisorDao;
        this.consultantSchedulesRepository = consultantSchedulesRepository;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Result<Admin> loginService(Integer AdminId, String password) {
        String salt = adminDao.getSaltById(AdminId);
        if(salt==null){                 //如果查不到盐值，即账号不存在
            return Result.error("2","账号不存在");
        }
        try {
            String hashPassword = PasswordHashWithSalt.hashPassword(password, salt);  //根据盐值和输入的密码进行哈希
            Admin admin = adminDao.getByIdAndPassword(AdminId, hashPassword);        //根据账号id和哈希后的密码查询管理员
            if(admin==null){                    //查询不到即为密码错误
                return Result.error("2","密码错误");
            }
            else{
                Map<String,Object> claims = new HashMap<>();
                claims.put("type","admin");
                claims.put("id",admin.getAdminId());
                admin.setToken(JwtUtil.generateJwt(claims));
                return Result.success(admin);
            }
        } catch (Exception e) {
            log.error("Admin login service exception:", e);
            return Result.error("未知异常");
        }
    }

    public Result<Consultant> addConsultantService(Consultant consultant) {
        consultant.setConsultantId(IdGenerator.generateId());
        String password = consultant.getPassword();
        String salt = PasswordHashWithSalt.generateSalt();
        consultant.setSalt(salt);
        try{
            String hashPassword = PasswordHashWithSalt.hashPassword(password, salt);
            consultant.setPassword(hashPassword);
        }catch(Exception e){
            return Result.error("未知异常");
        }
        consultantDao.addConsultant(consultant);
        return Result.success(consultant);
    }

    public Result<Supervisor> addSupervisorService(Supervisor supervisor) {
        supervisor.setSupervisorId(IdGenerator.generateId());
        String salt = PasswordHashWithSalt.generateSalt();
        supervisor.setSalt(salt);
        try{
            String hashPassword = PasswordHashWithSalt.hashPassword(supervisor.getPassword(), salt);
            supervisor.setPassword(hashPassword);
        }catch(Exception e){
            return Result.error("未知异常");
        }
        supervisorDao.addSupervisor(supervisor);
        return Result.success(supervisor);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<List<Map<String,Object>>> manageConsultantSchedule(List<Map<String,Object>> scheduleList) {
        List<String> addedRedisKeys = new ArrayList<>();
        List<String> addedRedisValues = new ArrayList<>();
        try {
            for (Map<String, Object> map : scheduleList) {
                ConsultantSchedule schedule = generateConsultantSchedule(map);
                if (schedule == null) {
                    throw new IllegalArgumentException("参数错误");
                }

                String day = (String) map.get("day");

                String key = ScheduleGenerator.getConsultantKey((String) map.get("time"), day);   //存入Redis并检查重复情况
                String value = schedule.getConsultantId() + ":" + map.get("name");
                if (redisTemplate.opsForSet().add(key, value) == 0L) {
                    throw new IllegalStateException("存在督导在某时间段已有排班");
                }
                redisTemplate.expire(key, 30L, TimeUnit.DAYS);
                addedRedisKeys.add(key);
                addedRedisValues.add(value);

                for (LocalDate date : ScheduleGenerator.generateDate(day)) {  //生成日期
                    schedule.setAvailableDate(date);
                    if (consultantSchedulesRepository.insertSchedule(schedule) == 0) {
                        throw new RuntimeException("排班失败");
                    }
                }
            }
            return Result.success(null);
        } catch (Exception e) {
            for (int i = 0; i < addedRedisKeys.size(); i++) {
                redisTemplate.opsForSet().remove(addedRedisKeys.get(i), addedRedisValues.get(i));
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<List<Map<String,Object>>> manageSupervisorSchedule(List<Map<String,Object>> scheduleList) {
        List<String> addedRedisKeys = new ArrayList<>();
        List<String> addedRedisValues = new ArrayList<>();
        try {
            for (Map<String, Object> map : scheduleList) {
                SupervisorSchedule schedule = generateSupervisorSchedule(map);
                if (schedule == null) {
                    throw new IllegalArgumentException("参数错误");
                }

                String day = (String) map.get("day");
                String key = ScheduleGenerator.getSupervisorKey((String) map.get("time"), day);
                String value = schedule.getSupervisorId() + ":" + map.get("name");
                if (redisTemplate.opsForSet().add(key, value) == 0L) {
                    throw new IllegalStateException("存在督导在某时间段已有排班");
                }
                redisTemplate.expire(key, 30L, TimeUnit.DAYS);
                addedRedisKeys.add(key);
                addedRedisValues.add(value);

                for (LocalDate date : ScheduleGenerator.generateDate(day)) {
                    schedule.setAvailableDate(date);
                    if (supervisorDao.insertSchedule(schedule) == 0) {
                        throw new RuntimeException("排班失败");
                    }
                }
            }
            return Result.success(null);
        }catch(Exception e){
            for (int i = 0; i < addedRedisKeys.size(); i++) {
                redisTemplate.opsForSet().remove(addedRedisKeys.get(i), addedRedisValues.get(i));
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Consultant> deleteConsultant(Integer id) {
        if(consultantDao.dismissal(id)==0){
            return Result.error("删除失败");
        }
        return Result.success();
    }

    @Override
    public Result<Supervisor> deleteSupervisor(Integer id) {
        if(supervisorDao.dismissal(id)==0){
            return Result.error("删除失败");
        }
        return Result.success();
    }

    @Override
    public Result<Set<String>> getConsultantNextSchedule(String day, String time) {
        String key = ScheduleGenerator.getConsultantKey(time,day);
        Set<String> set = redisTemplate.opsForSet().members(key);
        if(set==null){
            return Result.error("获取失败，该时间段暂无排班");
        }
        return Result.success(set);
    }

    @Override
    public Result<Set<String>> getSupervisorNextSchedule(String day, String time) {
        String key = ScheduleGenerator.getSupervisorKey(time,day);
        Set<String> set = redisTemplate.opsForSet().members(key);
        if(set==null){
            return Result.error("获取失败，该时间段暂无排班");
        }
        return Result.success(set);
    }

    @Override
    public Result<Map<String, Object>> deleteSupervisorSchedule(Map<String, Object> map) {
        SupervisorSchedule schedule = generateSupervisorSchedule(map);
        if(schedule==null){
            return Result.error("参数错误");
        }
        String day = (String)map.get("day");

        String key = ScheduleGenerator.getSupervisorKey((String)map.get("time"),day);
        String value = schedule.getSupervisorId()+":"+map.get("name");
        if(redisTemplate.opsForSet().remove(key,value )==0L){
            return Result.error("不存在该时间段排班");
        }
        redisTemplate.expire(key, 30L, TimeUnit.DAYS);

        for(LocalDate date:ScheduleGenerator.generateDate(day)){
            schedule.setAvailableDate(date);
            if(supervisorDao.deleteSchedule(schedule)==0){
                // 手动回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("删除排班失败");
            }
        }
        return Result.success(map);
    }

    @Override
    public Result<Map<String, Object>> deleteConsultantSchedule(Map<String, Object> map) {
        ConsultantSchedule schedule = generateConsultantSchedule(map);
        if(schedule==null){
            return Result.error("参数错误");
        }

        String day = (String)map.get("day");

        String key = ScheduleGenerator.getConsultantKey((String)map.get("time"),day);   //在Redis中检查是否存在且删除
        String value = schedule.getConsultantId()+":"+map.get("name");
        if(redisTemplate.opsForSet().remove(key,value)==0L){
            return Result.error("不存在该时间段排班");
        }
        for(LocalDate date:ScheduleGenerator.generateDate(day)){  //生成日期
            schedule.setAvailableDate(date);
            if(consultantSchedulesRepository.deleteConsultantSchedule(schedule)==0){
                // 手动回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("删除排班失败");
            }
        }
        return Result.success(map);
    }


    private SupervisorSchedule generateSupervisorSchedule(Map<String,Object> map){
        SupervisorSchedule schedule = new SupervisorSchedule();
        Integer supervisorId = (Integer)map.get("supervisorId");
        String Time = (String)map.get("time");
        String day = (String)map.get("day");
        String name = (String)map.get("name");
        if(supervisorId ==null || Time==null || day==null || name==null){
            return null;
        }
        schedule.setSupervisorId(supervisorId);
        int[] time = ScheduleGenerator.getTime(Time);   //获取开始时间和结束时间
        schedule.setStartTime(time[0]);
        schedule.setEndTime(time[1]);
        return schedule;
    }

    private ConsultantSchedule generateConsultantSchedule(Map<String,Object> map){
        Integer consultantId = (Integer)map.get("consultantId");
        String Time = (String)map.get("time");
        String day = (String)map.get("day");
        String name = (String)map.get("name");
        if(consultantId==null || Time==null || day==null || name==null){
            return null;
        }
        ConsultantSchedule schedule = new ConsultantSchedule();
        schedule.setConsultantId(consultantId);

        int[] time = ScheduleGenerator.getTime(Time);  //处理时间
        schedule.setStartTime(time[0]);
        schedule.setEndTime(time[1]);
        return schedule;
    }
}
