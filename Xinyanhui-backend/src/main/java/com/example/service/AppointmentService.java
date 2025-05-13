package com.example.service;

import com.example.pojo.Appointment;
import com.example.pojo.AppointmentStatus;
import com.example.utils.Result;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Result<Appointment> bookAppointment(Appointment appointment);

    // 查询用户预约
    Result<List<Appointment>> getUserAppointments(Integer userId, LocalDate startDate, LocalDate endDate, AppointmentStatus appointmentStatus);

    Result<List<Appointment>> getConsultantAppointments(Integer consultantId, LocalDate startDate, LocalDate endDate, AppointmentStatus appointmentStatus);

    //取消预约
    Result cancelAppointment(Integer appointmentId, String reason);

}