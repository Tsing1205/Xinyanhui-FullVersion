package com.example.repository;

import com.example.pojo.Appointment;
import com.example.pojo.AppointmentStatus;
import com.example.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentRepository {
    @Select("SELECT * FROM Appointments WHERE user_id = #{userId} AND appointment_date BETWEEN #{startDate} AND #{endDate}")
    List<Appointment> findByUserIdAndAppointmentDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    @Select("SELECT * FROM Appointments WHERE consultant_id = #{consultantId} AND appointment_date BETWEEN #{startDate} AND #{endDate}")
    List<Appointment> findByConsultantIdAndAppointmentDateBetween(Integer consultantId, LocalDate startDate, LocalDate endDate);

    @Select("SELECT * FROM Appointments WHERE appointment_id = #{appointmentId} AND status = #{status}")
    Optional<Appointment> findByAppointmentIdAndStatus(Integer appointmentId, AppointmentStatus status);

    @Select("SELECT COUNT(*) FROM Appointments WHERE consultant_id = #{consultantId} AND appointment_date = #{appointmentDate} AND appointment_time = #{appointmentTime}")
    Long countByConsultantIdAndAppointmentDateAndAppointmentTime(Integer consultantId, LocalDate appointmentDate, LocalTime appointmentTime);

    @Select("SELECT COUNT(*) FROM Appointments WHERE user_id = #{userId} AND appointment_date = #{appointmentDate} AND appointment_time = #{appointmentTime}")
    Long countByUserIdAndAppointmentDateAndAppointmentTime(Integer userId, LocalDate appointmentDate, LocalTime appointmentTime);

    @Select("SELECT name FROM Consultants WHERE consultant_id = #{consultantId}")
    String findConsultantNameById(Integer consultantId);

    @Update("UPDATE Appointments SET status = 'canceled', cancellation_reason = #{reason},cancellation_time = NOW() WHERE appointment_id = #{appointmentId} AND status = 'booked'")
    int cancelAppointment(@Param("appointmentId") Integer appointmentId, @Param("reason") String reason);

    @Insert("INSERT INTO Appointments (user_id, consultant_id, appointment_date, appointment_time, booking_date, status, cancellation_time, cancellation_reason) " +
            "VALUES (#{userId}, #{consultantId}, #{appointmentDate}, #{appointmentTime}, #{bookingDate}, #{status}, #{cancellationTime}, #{cancellationReason})")
    void save(Appointment appointment);

    @Select("SELECT * FROM Appointments WHERE appointment_date > #{localDate} AND appointment_time > #{localTime}")
    List<Appointment> findUpcomingAppointments(LocalDate localDate, LocalTime localTime);

    @Select("SELECT COUNT(*) FROM Appointments WHERE user_id = #{userId} AND appointment_date >= #{start} AND appointment_date <= #{end} AND(status='booked' OR status ='completed')" )
    int countAppointmentsByDateRange(Integer userId,LocalDate start, LocalDate end);

    @Select("SELECT * FROM Appointments WHERE appointment_id = #{appointmentId}")
    Appointment findById(Integer appointmentId);

    @Select("SELECT * FROM Appointments WHERE consultant_id = #{id} AND appointment_date = #{date} AND appointment_time >= #{start} AND appointment_time <= #{end}")
    List<Appointment> findByConsultantSchedule(Integer id,  LocalDate date,LocalTime start, LocalTime end);

    @Select("SELECT username FROM Users WHERE user_id = #{userId}")
    String findUserNameByUserId(Integer userId);
}