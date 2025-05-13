package com.example.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.example.pojo.ChatMsg;
import com.example.service.ObserveService;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ObserveServiceImpl implements ObserveService {
    private final static Map<Integer,Session> observerSessions = new ConcurrentHashMap<>();
    private final static Map<Session,Object> sessionLocks = new ConcurrentHashMap<>();

    @Override
    public void addObservedSession(int id, Session session) {
        observerSessions.put(id, session);
        sessionLocks.put(session, new Object());
    }

    @Override
    public void removeObservedSession(int id) {
        Session session = observerSessions.remove(id);
        sessionLocks.remove(session);
    }

    @Override
    public void sendMessage(int id, String msg) {
        Session session = observerSessions.get(id);
        if (session != null) {
            Object lock = sessionLocks.get(session);
            if(lock!=null){
                synchronized (lock){
                    try {
                        session.getBasicRemote().sendText(msg);
                    } catch (Exception e) {
                        log.error("[SEND] sessionId={}, session={}, message={}", id, session.getId(), msg);
                    }
                }
            }
        }
    }

    @Override
    public boolean isObserved(int id) {
        return observerSessions.containsKey(id);
    }

    @Override
    public void observedSessionClosed(int id) {
        if(!isObserved(id)){
            return;
        }
        Session session = observerSessions.get(id);
        ChatMsg msg = new ChatMsg("会话已结束，监督连接断开", null, LocalDateTime.now());
        sendMessage(id, JSON.toJSONString(msg));
        try {
            session.close();
        } catch (IOException e) {
            log.error("会话已结束，监督连接断开出错。异常:{}",e.getMessage());
        }
    }
}
