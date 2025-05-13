package com.example.controller;

import com.example.constants.TypeConstant;
import com.example.pojo.Notification;
import com.example.service.NotificationService;
import com.example.utils.JwtUtil;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/notification/newnum")
    public Result<Long> getNewNotificationCount(@RequestParam Integer userId) {
        return notificationService.getNewNotificationCount(userId, TypeConstant.USER_STR2);
    }

    @GetMapping("/user/notification/list")
    public Result<List<Notification>> getNotifications(@RequestParam Integer userId) {
        return notificationService.getNotifications(userId, TypeConstant.USER_STR2);
    }

    @PutMapping("/user/notification")
    public Result<Notification> readNotification(@RequestParam Integer notfId) {
        return notificationService.readNotification(notfId);
    }

    @DeleteMapping("/user/notification")
    public Result<Notification> deleteNotification(@RequestParam Integer notfId) {
        return notificationService.deleteNotification(notfId);
    }

    @GetMapping("/internal/notification/newnum")
    public Result<Long> getNewNotificationCount(@RequestHeader String token) {
        Map<String, Object> params = JwtUtil.parseJwt(token);
        String role = (String) params.get("type");
        role = getRole(role);
        if(role==null){
            return Result.error("Invalid role");
        }
        return notificationService.getNewNotificationCount((Integer) params.get("id"), role);
    }

    @GetMapping("/internal/notification/list")
    public Result<List<Notification>> getNotifications(@RequestHeader String token) {
        Map<String, Object> params = JwtUtil.parseJwt(token);
        String role = (String) params.get("type");
        role = getRole(role);
        if(role==null){
            return Result.error("Invalid role");
        }
        return notificationService.getNotifications((Integer) params.get("id"), role);
    }

    @PutMapping("/internal/notification")
    public Result<Notification> readNotificationByInternal(@RequestParam Integer notfId) {
        return notificationService.readNotification(notfId);
    }

    @DeleteMapping("/internal/notification")
    public Result<Notification> deleteNotificationByInternal(@RequestParam Integer notfId) {
        return notificationService.deleteNotification(notfId);
    }

    private String getRole(String originRole) {
        String role = originRole;
        switch (role) {
            case TypeConstant.USER_STR:
                role = TypeConstant.USER_STR2;
                break;
            case TypeConstant.CONSULTANT_STR:
                role = TypeConstant.CONSULTANT_STR2;
                break;
            case TypeConstant.SUPERVISOR_STR:
                role = TypeConstant.SUPERVISOR_STR2;
                break;
            case TypeConstant.ADMIN_STR:
                role = TypeConstant.ADMIN_STR2;
                break;
            default:
                return null;
        }
        return role;
    }
}
