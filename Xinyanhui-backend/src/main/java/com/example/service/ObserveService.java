package com.example.service;

import jakarta.websocket.Session;

public interface ObserveService {
    void addObservedSession(int id, Session session);
    void removeObservedSession(int id);
    void sendMessage(int id, String msg);
    boolean isObserved(int id);
    void observedSessionClosed(int id);
}
