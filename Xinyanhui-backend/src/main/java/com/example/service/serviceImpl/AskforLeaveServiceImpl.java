package com.example.service.serviceImpl;

import com.example.constants.TypeConstant;
import com.example.pojo.Appointment;
import com.example.pojo.ConsultantSchedule;
import com.example.repository.AppointmentRepository;
import com.example.repository.ConsultantDao;
import com.example.repository.ConsultantSchedulesRepository;
import com.example.service.AskforLeaveService;
import com.example.service.NotificationService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.utils.NotifyContentUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AskforLeaveServiceImpl implements AskforLeaveService {

    @Autowired
    private ConsultantSchedulesRepository consultantSchedulesRepository;

    @Autowired
    private ConsultantDao consultantDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AppointmentRepository appointmentDao;

    @Override
    public Result askforLeave(Integer consultantId, String date, String time, String cancellationReason) {
        int hour;
        if (time.equals("AM")) {
            hour = 8;  // AM
        }
        else if(time.equals("PM")){
            hour = 13; // PM
        }
        else{
            return Result.error("参数错误");
        }

        // 格式化请假信息，并存入 note 字段
        String leaveNote = String.format("Date: %s, Time: %d, Reason: %s", date, hour, cancellationReason);

        // 更新 ConsultantSchedules 表格中对应 consultantId 和时间段的 status 为 request，并将请假信息存入 note
        int rowsAffected = consultantSchedulesRepository.updateLeaveRequest(consultantId, date, hour, leaveNote);

        if (rowsAffected > 0) {
            // 发送通知给管理员
            Map<String, Object> map = new HashMap<>();
            map.put("咨询师", consultantDao.getNameById(consultantId));
            map.put("请假信息", leaveNote);
            String []content = NotifyContentUtil.requestNotificationOfAdmin(map);
            notificationService.sendAdminNotification(content);
            return Result.success("请假申请成功");
        } else {
            return Result.error("2", "请假申请失败");
        }
    }

    @Override
    public Result approveLeave(Integer scheduleId, Boolean isApproved) {
        try {
            int rowsAffected;
            String approvalMessage;
            if (isApproved) {
                // 审批通过，更新状态为 'leave'
                rowsAffected = consultantSchedulesRepository.updateLeaveApproved(scheduleId);
                if(rowsAffected == 0){
                    return Result.error("2", "请假申请不存在或状态无法审批");
                }
                //发送通知给咨询师
                Map<String, Object> map = new HashMap<>();
                ConsultantSchedule cs = consultantSchedulesRepository.getConsultantScheduleByScheduleId(scheduleId);
                map.put("请假时间",  cs.getAvailableDate()+" "+cs.getStartTime()+":00"+"-"+cs.getEndTime()+":00");
                notificationService.sendNotification(cs.getConsultantId(),TypeConstant.CONSULTANT_STR2, NotifyContentUtil.approveNotificationOfConsultant(map));
                sendNotificationToUser(cs);
                //发送通知给用户
                return Result.success("请假审批成功");
            } else {
                // 审批拒绝，更新状态为 'rejected'
                rowsAffected = consultantSchedulesRepository.updateLeaveRejected(scheduleId);
                if(rowsAffected == 0){
                    return Result.error("2", "请假申请不存在或状态无法审批");
                }
                approvalMessage = "请假申请已拒绝";
                //发送通知给咨询师
                Map<String, Object> map = new HashMap<>();
                ConsultantSchedule cs = consultantSchedulesRepository.getConsultantScheduleByScheduleId(scheduleId);
                map.put("请假时间",  cs.getAvailableDate()+" "+cs.getStartTime()+":00"+"-"+cs.getEndTime()+":00");
                notificationService.sendNotification(cs.getConsultantId(),TypeConstant.CONSULTANT_STR2, NotifyContentUtil.rejectNotificationOfConsultant(map));
                return Result.success("请假审批已拒绝");
            }
        } catch (Exception e) {
            return Result.error("500", "审批过程中出现错误：" + e.getMessage());
        }
    }

    @Override
    public Result viewLeave() {
        String status = "request";
        List<ConsultantSchedule> schedules = consultantSchedulesRepository.getScheduleByStatus(status);

        if(schedules == null ){
            return Result.error("2", "请假申请查询失败");
        }else if(schedules.isEmpty()){
            return Result.error("2", "没有请假待审批");
        }


        return Result.success(schedules);
    }

    public void sendNotificationToUser(ConsultantSchedule cs){
        Integer consultantId=cs.getConsultantId();
        LocalDate date=cs.getAvailableDate();
        LocalTime startTime = LocalTime.of(cs.getStartTime(),0);
        LocalTime endTime = LocalTime.of(cs.getEndTime(),0);
        List<Appointment> appointments = appointmentDao.findByConsultantSchedule(consultantId,date,startTime,endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("咨询师", consultantDao.getNameById(consultantId));
        for(Appointment appointment:appointments){
            map.put("时间",appointment.getAppointmentDate() + " " +  appointment.getAppointmentTime());
            notificationService.sendNotification(appointment.getUserId(),TypeConstant.USER_STR2, NotifyContentUtil.approveNotificationOfUser(map));
        }
    }
}
