package com.example.service;

import jakarta.websocket.Session;

import java.io.IOException;

public interface ChatService {
    void addSession(String sessionId, int id,String userType,int sessionType, Session session);
    void removeSession(String sessionId, int id,String userType,int sessionType);
    void sendMessage(String sessionId,int senderId,String userType, int sessionType,String message);
    void broadcast(String sessionId, int sessionType,String message) throws IOException;
    void broadcast(String message) throws IOException;
    void removeAbnormalSession(String sessionId);
}
