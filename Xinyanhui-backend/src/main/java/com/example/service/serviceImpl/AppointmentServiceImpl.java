package com.example.service.serviceImpl;

import com.example.constants.TypeConstant;
import com.example.pojo.Appointment;
import com.example.pojo.AppointmentStatus;
import com.example.repository.AppointmentRepository;
import com.example.repository.ConsultantDao;
import com.example.repository.UserDao;
import com.example.service.AppointmentService;
import com.example.service.NotificationService;
import com.example.utils.NotifyContentUtil;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ConsultantDao  consultantDao;
    @Autowired
    private UserDao userDao;

    // 预约咨询
    public Result<Appointment> bookAppointment(Appointment appointment) {
        // 检查预约信息是否完整
        if (appointment.getUserId() == null || appointment.getConsultantId() == null
                || appointment.getAppointmentDate() == null || appointment.getAppointmentTime() == null) {
            return Result.error("预约信息不完整");
        }

        // 检查同一时间段内该咨询师的预约数量
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime appointmentTime = appointment.getAppointmentTime();
        Long appointmentCount = appointmentRepository.countByConsultantIdAndAppointmentDateAndAppointmentTime(
                appointment.getConsultantId(), appointmentDate, appointmentTime);

        if (appointmentCount >= 5) {
            return Result.error("同一时间段内该咨询师的预约已满，请选择其他时间");
        }

        // 检查用户是否已在同一时间段预约了其他咨询师
        Long userAppointmentCount = appointmentRepository.countByUserIdAndAppointmentDateAndAppointmentTime(
                appointment.getUserId(), appointmentDate, appointmentTime);

        if (userAppointmentCount > 0) {
            return Result.error("您已在该时间段预约了其他咨询师，请选择其他时间");
        }

        int count = appointmentRepository.countAppointmentsByDateRange(appointment.getUserId(),appointmentDate.minusDays(3),appointmentDate.plusDays(3));
        if(count>0){
            return Result.error("两次预约时间段间隔需在三天内,您在该时间段已有预约，请选择其他时间");
        }
        // 设置预约状态为 "已预约"
        appointment.setStatus(AppointmentStatus.booked);

        // 设置预约创建时间
        appointment.setBookingDate(LocalDateTime.now());

        // 取消时间和取消原因默认为null
        appointment.setCancellationTime(null);
        appointment.setCancellationReason(null);

        // 保存预约信息
        appointmentRepository.save(appointment);

        // 发送预约通知
        Map<String, Object> map = new HashMap<>();
        map.put("用户", userDao.findNameById(appointment.getUserId()));
        map.put("咨询师", consultantDao.getNameById(appointment.getConsultantId()));
        map.put("时间", appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
        String []content1 = NotifyContentUtil.bookNotificationOfUser(map);
        notificationService.sendNotification(appointment.getUserId(), TypeConstant.USER_STR2, content1);
        String []content2 = NotifyContentUtil.bookNotificationOfConsultant(map);
        notificationService.sendNotification(appointment.getConsultantId(), TypeConstant.CONSULTANT_STR2, content2);
        return Result.success(appointment);
    }

    // 查询用户预约
    @Override
    public Result<List<Appointment>> getUserAppointments(Integer userId, LocalDate startDate, LocalDate endDate, AppointmentStatus appointmentStatus) {
        startDate = (startDate != null) ? startDate : LocalDate.MIN;
        endDate = (endDate != null) ? endDate : LocalDate.now().plusWeeks(1);

        // 查询用户的所有预约记录
        List<Appointment> appointments = appointmentRepository.findByUserIdAndAppointmentDateBetween(userId, startDate, endDate);
        if (appointments == null || appointments.isEmpty()) {
            return Result.error("2", "用户预约记录不存在");
        }

        // 如果 appointmentStatus 不为空，则过滤出对应状态的预约记录
        if (appointmentStatus != null) {
            appointments = appointments.stream()
                    .filter(appointment -> appointmentStatus.equals(appointment.getStatus()))
                    .collect(Collectors.toList());
        }

        // 获取每个预约的咨询师姓名
        for (Appointment appointment : appointments) {
            String consultantName = appointmentRepository.findConsultantNameById(appointment.getConsultantId());
            appointment.setConsultantName(consultantName);
        }

        for (Appointment appointment : appointments) {
            String userName = appointmentRepository.findUserNameByUserId(appointment.getUserId());
            appointment.setUserName(userName);
        }

        return Result.success(appointments, "查询成功");
    }

    // 查询咨询师预约
    @Override
    public Result<List<Appointment>> getConsultantAppointments(Integer consultantId, LocalDate startDate, LocalDate endDate, AppointmentStatus appointmentStatus) {
        startDate = (startDate != null) ? startDate : LocalDate.MIN;
        endDate = (endDate != null) ? endDate : LocalDate.now().plusWeeks(1);

        // 查询用户的所有预约记录
        List<Appointment> appointments = appointmentRepository.findByConsultantIdAndAppointmentDateBetween(consultantId, startDate, endDate);
        if (appointments == null || appointments.isEmpty()) {
            return Result.error("2", "咨询师预约记录不存在");
        }

        // 如果 appointmentStatus 不为空，则过滤出对应状态的预约记录
        if (appointmentStatus != null) {
            appointments = appointments.stream()
                    .filter(appointment -> appointmentStatus.equals(appointment.getStatus()))
                    .collect(Collectors.toList());
        }

        for (Appointment appointment : appointments) {
            String consultantName = appointmentRepository.findConsultantNameById(appointment.getConsultantId());
            appointment.setConsultantName(consultantName);
        }

        for (Appointment appointment : appointments) {
            String userName = appointmentRepository.findUserNameByUserId(appointment.getUserId());
            appointment.setUserName(userName);
        }

        return Result.success(appointments, "查询成功");
    }

    // 取消预约
    @Override
    public Result cancelAppointment(Integer appointmentId, String reason) {
        // 直接尝试更新状态
        int rowsUpdated = appointmentRepository.cancelAppointment(appointmentId, reason);
        // 如果更新失败（没有符合条件的记录）
        if (rowsUpdated == 0) {
            return Result.error("预约不存在或状态无法取消");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId);
        // 发送取消通知
        Map<String, Object> map = new HashMap<>();
        map.put("用户", userDao.findNameById(appointment.getUserId()));
        map.put("咨询师", consultantDao.getNameById(appointment.getConsultantId()));
        map.put("时间", appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
        String []content1 = NotifyContentUtil.cancelNotificationOfUser(map);
        notificationService.sendNotification(appointment.getUserId(), TypeConstant.USER_STR2, content1);
        String []content2 = NotifyContentUtil.cancelNotificationOfConsultant(map);
        notificationService.sendNotification(appointment.getConsultantId(), TypeConstant.CONSULTANT_STR2, content2);
        // 更新成功
        return Result.success("预约已成功取消");
    }
}