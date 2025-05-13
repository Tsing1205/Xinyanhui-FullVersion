package com.example.service;

import com.example.pojo.NotifyMsg;
import jakarta.websocket.Session;


public interface NotifyService {

    void addSession(int id, String type, Session session);
    void removeSession(int id, String type);
    void sendMessage(int id, int type, NotifyMsg msg);
}
