package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.ConsultationSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultationSessionDao extends BaseMapper<ConsultationSession> {

    @Select("select session_id from ConsultationSessions where appointment_id=#{appointmentId}")
    Integer findIdByAppointmentId(Integer appointmentId);

   /* @Results({
            @Result(property = "session_id", column = "session_id"),
            @Result(property = "user_id", column = "user_id"),
            @Result(property= "username",  column = "username"),
            @Result(property = "consultant_id", column = "consultant_id"),
            @Result(property= "name",  column = "name"),
            @Result(property = "start_time", column = "start_time"),
    })*/
    List<Map<String,Object>> getAbnormalConsultation();

    List<Map<String,Object>> getWeeklyConsultation(Integer num);

    List<Map<String,Object>> getSessionWithNameByConsultantId(Integer consultantId, LocalDateTime startTime, LocalDateTime endTime);

    List<Map<String,Object>> getSessionWithNameByUserId(Integer userId,LocalDateTime startTime, LocalDateTime endTime);

}
