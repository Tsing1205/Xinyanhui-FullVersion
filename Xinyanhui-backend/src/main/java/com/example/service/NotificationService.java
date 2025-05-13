package com.example.service;

import com.example.pojo.Notification;
import com.example.utils.Result;

import java.util.List;

public interface NotificationService {
    Result<Long> getNewNotificationCount(Integer Id, String role);
    Result<List<Notification>> getNotifications(Integer Id, String role);
    Result<Notification> readNotification(Integer id);
    Result<Notification> deleteNotification(Integer id);
    boolean sendNotification(Integer recId,String recRole,String content);
    void sendNotification(Integer recId,String recRole,String[] content);
    void sendAdminNotification(String[] content);
}
