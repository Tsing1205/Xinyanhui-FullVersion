package com.example.repository;

import com.example.pojo.ConsultantSchedule;
import com.example.pojo.SupervisorSchedule;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ConsultantSchedulesRepository {
    @Update("UPDATE ConsultantSchedules SET status = 'request', note = #{leaveNote} WHERE consultant_id = #{consultantId} AND available_date = #{date} AND start_time = #{hour}")
    int updateLeaveRequest(Integer consultantId, String date, Integer hour, String leaveNote);

    @Update("UPDATE ConsultantSchedules SET status = 'leave' WHERE schedule_id = #{scheduleId}")
    int updateLeaveApproved(Integer scheduleId);

    @Update("UPDATE ConsultantSchedules SET status = 'normal' WHERE schedule_id = #{scheduleId}")
    int updateLeaveRejected(Integer scheduleId);

    @Insert("INSERT INTO ConsultantSchedules (consultant_id, available_date, start_time, end_time) VALUES (#{consultantId}, #{availableDate}, #{startTime}, #{endTime})")
    int insertSchedule(ConsultantSchedule schedule);

    @Select("SELECT * FROM ConsultantSchedules WHERE status = #{status}")
    List<ConsultantSchedule> getScheduleByStatus(String status);

    @Select("SELECT * FROM ConsultantSchedules WHERE consultant_id = #{consultantId} AND available_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 2 MONTH) AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH)")
    List<ConsultantSchedule> getConsultantScheduleById(Integer consultantId);

    @Select("SELECT * FROM SupervisorSchedules WHERE supervisor_id = #{supervisorId} AND available_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 2 MONTH) AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH)")
    List<SupervisorSchedule> getSupervisorScheduleById(Integer supervisorId);

    @Select("SELECT * FROM ConsultantSchedules WHERE schedule_id = #{scheduleId} AND available_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 2 MONTH) AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH)")
    ConsultantSchedule getConsultantScheduleByScheduleId(Integer scheduleId);

    @Delete("DELETE FROM ConsultantSchedules WHERE consultant_id=#{consultantId} AND available_date=#{availableDate} AND start_time=#{startTime}")
    Integer deleteConsultantSchedule(ConsultantSchedule schedule);


}