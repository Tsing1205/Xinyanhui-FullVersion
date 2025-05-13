package com.example.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.example.constants.TypeConstant;
import com.example.pojo.NotifyMsg;
import com.example.service.NotifyService;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    private static final Map<Integer,Session> onlineUsers = new ConcurrentHashMap<>();
    private static final Map<Integer,Session> onlineConsultants = new ConcurrentHashMap<>();
    private static final Map<Integer,Session> onlineSupervisors = new ConcurrentHashMap<>();
    private static final Map<Integer,Session> onlineAdmins = new ConcurrentHashMap<>();
    private static final Map<Session,Object> sessionLocks = new ConcurrentHashMap<>();

    @Override
    public void addSession(int id, String type, Session session) {  //type是指用户类型
        if(type.equals(TypeConstant.USER_STR)){
            onlineUsers.put(id, session);
        }
        else if(type.equals(TypeConstant.CONSULTANT_STR)){
            onlineConsultants.put(id, session);
        }
        else if(type.equals(TypeConstant.SUPERVISOR_STR)){
            onlineSupervisors.put(id, session);
        }
        else if(type.equals(TypeConstant.ADMIN_STR)){
            onlineAdmins.put(id, session);
        }
        sessionLocks.put(session, new Object());
    }

    @Override
    public void removeSession(int id, String type) {
        Session session=null;
        if(type.equals(TypeConstant.USER_STR)){
            session = onlineUsers.remove(id);
        }
        else if(type.equals(TypeConstant.CONSULTANT_STR)){
            session = onlineConsultants.remove(id);
        }
        else if(type.equals(TypeConstant.SUPERVISOR_STR)){
            session = onlineSupervisors.remove(id);
        }
        else if(type.equals(TypeConstant.ADMIN_STR)){
            session = onlineAdmins.remove(id);
        }
        sessionLocks.remove(session);
    }

    @Override
    public void sendMessage(int id, int type, NotifyMsg msg) {   //type是指用户类型
        Session session = null;
        if(type==TypeConstant.USER_INT){
            session = onlineUsers.get(id);
        }
        else if(type==TypeConstant.CONSULTANT_INT){
            session = onlineConsultants.get(id);
        }
        else if(type==TypeConstant.SUPERVISOR_INT){
            session = onlineSupervisors.get(id);
        }
        else if(type==TypeConstant.ADMIN_INT){
            session = onlineAdmins.get(id);
        }

        if(session!=null) {
            Object lock = sessionLocks.get(session);
            if(lock!=null){
                synchronized (lock){
                    try{
                        session.getBasicRemote().sendText(JSON.toJSONString(msg));
                    }catch(Exception e){
                        log.error("发送消息失败");
                    }
                }
            }

        }
    }
}
