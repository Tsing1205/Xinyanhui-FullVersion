package com.example.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.constants.ChatConstant;
import com.example.pojo.ChatMsg;
import com.example.pojo.ConSessionStatus;
import com.example.pojo.ConsultationSession;
import com.example.pojo.SupervisorConsultation;
import com.example.repository.*;
import com.example.service.ChatService;
import com.example.service.ObserveService;
import com.example.utils.WsChatMsgUtil;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();//thread safe map
    private static final Map<String, Session> onlineConsultants = new ConcurrentHashMap<>();
    private static final Map<String, Session> onlineSupervisors = new ConcurrentHashMap<>();
    // RedisTemplate
    private final  StringRedisTemplate redisTemplate;
    private final ConsultationSessionDao consultationSessionDao;
    private final SupervisorConsultationDao supervisorConsultationDao;
    private ChatLogDao chatLogDao;
    private SupervisorDao supervisorDao;
    private UserDao userDao;
    private ConsultantDao consultantDao;
    private final ObserveService observeService;

    private String COMPLETED_SESSION_KEY = "chat:session:completed:0";
    private String COMPLETED_SUPERVISOR_KEY = "chat:session:completed:1";

    @Autowired
    public ChatServiceImpl(StringRedisTemplate redisTemplate, ConsultationSessionDao consultationSessionDao,
                           SupervisorConsultationDao supervisorConsultationDao, ObserveService observeService) {
        this.consultationSessionDao = consultationSessionDao;
        this.redisTemplate = redisTemplate;
        this.supervisorConsultationDao = supervisorConsultationDao;
        this.observeService = observeService;
    }

    @Autowired
    public void setChatLogDao(ChatLogDao chatLogDao) {
        this.chatLogDao = chatLogDao;
    }

    @Autowired
    public void setSupervisorDao(SupervisorDao supervisorDao) {
        this.supervisorDao = supervisorDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setConsultantDao(ConsultantDao consultantDao) {
        this.consultantDao = consultantDao;
    }

    @Override
    public void addSession(String sessionId, int id, String userType, int sessionType, Session session) {
        //判断是否是已完成的会话,是则关闭session
        if( (sessionType==0 && isSessionCompleted(sessionId)) || (sessionType==1 && isSupervisorCompleted(sessionId))){
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Session is completed"));
            } catch (Exception e) {
                log.error("Error closing session,exception:{}",e.getMessage());
            }
            return ;
        }
        String username =null;
        boolean isStart = false;
        if(userType.equals("user")){
            onlineUsers.put(sessionId, session);
            username = "来访者 "+userDao.findNameById(id);
            if(onlineConsultants.containsKey(0+":"+sessionId)){
                //修改数据库中的session状态和开启时间
                isStart = true;
                activeSession(sessionId);
            }
        }
        else if(userType.equals("supervisor")){
            onlineSupervisors.put(sessionId, session);
            username = "督导 "+supervisorDao.getNameById(id);
            //放入redis
            redisTemplate.opsForSet().add("chat:sessionSet:supervisor:"+id, sessionId);  //咨询师对话列表
            redisTemplate.expire("chat:sessionSet:supervisor:"+id, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            if(onlineConsultants.containsKey(1+":"+sessionId)){
                //修改数据库中的session状态和开启时间
                responseSupervise(sessionId);
                isStart = true;
            }
        }
        else if(userType.equals("consultant")){
            onlineConsultants.put(sessionType+":"+sessionId, session);
            username = "咨询师 "+consultantDao.getNameById(id);
            //放入redis
            redisTemplate.opsForSet().add("chat:sessionSet:consultant:"+id+":"+sessionType, sessionId);   //咨询师对话set
            redisTemplate.expire("chat:sessionSet:consultant:"+id+":"+sessionType, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            //修改数据库中的session状态和开启时间
            if(sessionType==0 && onlineUsers.containsKey(sessionId)){
                //修改数据库中的session状态和开启时间
                activeSession(sessionId);
                isStart = true;
            }
            else if(sessionType==1){
                //修改数据库中的session状态和开启时间
                requestSupervise(sessionId);
            }
        }
        //向对话中传递系统消息，该用户上线

        String message = WsChatMsgUtil.getWsChatMsgJson(true, username+"已进入会话", LocalDateTime.now());
        try {
            broadcast(sessionId, sessionType, message);
            if(isStart){
                //向对话中传递系统消息，该用户已开始咨询
                message = WsChatMsgUtil.getWsChatMsgJson(true, "会话已开始", LocalDateTime.now());
                broadcast(sessionId, sessionType, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSession(String sessionId, int id, String userType, int sessionType) {
        Session session=null;     //用于获取会话中另一方的session
        if(userType.equals("user")){
            onlineUsers.remove(sessionId);
            //修改数据库中的session状态和结束时间
            inactiveSession(sessionId);
            if(onlineConsultants.containsKey(ChatConstant.CONSULTANTION_TYPE+":"+sessionId)){
                session = onlineConsultants.get(ChatConstant.CONSULTANTION_TYPE+":"+sessionId);
            }
        }
        else if(userType.equals("supervisor")){
            onlineSupervisors.remove(sessionId);
            redisTemplate.opsForSet().remove("chat:sessionSet:supervisor:"+id, sessionId);
            if(onlineConsultants.containsKey(ChatConstant.SUPERVISE_TYPE+":"+sessionId)){
                session = onlineConsultants.get(ChatConstant.SUPERVISE_TYPE+":"+sessionId);
            }
        }
        else if(userType.equals("consultant")){
            onlineConsultants.remove(sessionType+":"+sessionId);
            //放入redis
            redisTemplate.opsForSet().remove("chat:sessionSet:consultant:"+id+":"+sessionType, sessionId);
            //修改数据库中的session状态和结束时间
            if(sessionType==0){
                inactiveSession(sessionId);
                if(onlineUsers.containsKey(sessionId)){
                    session = onlineUsers.get(sessionId);
                }
            }
            else{
                if(onlineSupervisors.containsKey(sessionId)){
                    session = onlineSupervisors.get(sessionId);
                }
            }
        }
        //向对话中传递系统消息,对话结束
        String message = WsChatMsgUtil.getWsChatMsgJson(true, "会话已结束", LocalDateTime.now());
        try {
            if(session!=null){
                broadcast(sessionId, sessionType, message);
                session.close();    //另一方的会话也要关闭
            }
        } catch (IOException e) {
            log.info("出现IO异常");
        }
        observeService.observedSessionClosed(Integer.parseInt(sessionId));
        //对聊天记录进行持久化操作
        try{
            saveChatLog(sessionId, sessionType);
        } catch (Exception e) {
            log.info("聊天记录持久化失败");
        }

    }

    @Override
    public void sendMessage(String sessionId, int senderId, String userType, int sessionType, String message) {
        Session session = null;
        if(userType.equals("user") || userType.equals("supervisor")){  //要发送消息给咨询师
            session = onlineConsultants.get(sessionType+":"+sessionId);
        }
        else if(sessionType==0){    //咨询师向来访发送消息
            session = onlineUsers.get(sessionId);
        }
        else{                      //咨询师向督导发送消息
            session = onlineSupervisors.get(sessionId);
        }

        message = JSON.parseObject(message, ChatMsg.class).getMsg();

        //缓存消息
        LocalDateTime time = LocalDateTime.now();
        ChatMsg chatMsg = new ChatMsg(message, userType.equals("consultant"), time);
        redisTemplate.opsForList().leftPush("chat:msg:list:"+sessionType+":"+sessionId, JSON.toJSONString(chatMsg));
        redisTemplate.opsForValue().set("chat:msg:last:"+sessionType+":"+sessionId, JSON.toJSONString(chatMsg), ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //发送消息
        String msg = WsChatMsgUtil.getWsChatMsgJson(false, message, time);
        try{
            session.getBasicRemote().sendText(msg);
            if(sessionType==ChatConstant.CONSULTANTION_TYPE && observeService.isObserved(Integer.parseInt(sessionId))){
                observeService.sendMessage(Integer.parseInt(sessionId), JSON.toJSONString(chatMsg));
            }
        }catch (IOException e) {
            log.error("出现IO异常，消息发送失败");
        }
    }

    @Override
    public void broadcast(String sessionId, int sessionType, String message) throws IOException {
        Session session  = onlineConsultants.get(sessionType+":"+sessionId);
        //发送消息
        if(session!=null) {
            session.getBasicRemote().sendText(message);
        }
        if(sessionType==0){
            session = onlineUsers.get(sessionId);
            //发送消息
            if(session!=null) {
                session.getBasicRemote().sendText(message);
            }
        }
        else if(sessionType==1){
            session = onlineSupervisors.get(sessionId);
            //发送消息
            if(session!=null) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    @Override
    public void broadcast(String message) throws IOException{

        for(Session session:onlineUsers.values()){
            //发送消息
            session.getBasicRemote().sendText(message);
        }
        for(Session session:onlineSupervisors.values()){
            //发送消息
            session.getBasicRemote().sendText(message);
        }
        for(Session session:onlineConsultants.values()){
            //发送消息
            session.getBasicRemote().sendText(message);
        }
    }

    @Override
    public void removeAbnormalSession(String sessionId) {  //移除异常会话,主要指咨询师未及时应答的情况
        onlineUsers.remove(sessionId);
        QueryWrapper<ConsultationSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", Integer.parseInt(sessionId));
        consultationSessionDao.delete(queryWrapper);
    }


    private void activeSession(String sessionId){
        ConsultationSession cs = new ConsultationSession();
        cs.setSessionId(Integer.parseInt(sessionId));
        cs.setStartTime(LocalDateTime.now());
        cs.setSessionStatus(ConSessionStatus.ACTIVE);
        consultationSessionDao.updateById(cs);
    }

    private void inactiveSession(String sessionId){
        ConsultationSession cs = new ConsultationSession();
        cs.setSessionId(Integer.parseInt(sessionId));
        cs.setEndTime(LocalDateTime.now());
        cs.setSessionStatus(ConSessionStatus.COMPLETED);
        consultationSessionDao.updateById(cs);
    }

    private void requestSupervise(String sessionId){
        SupervisorConsultation sc = new SupervisorConsultation();
        sc.setRecordId(Integer.parseInt(sessionId));
        sc.setRequestTime(LocalDateTime.now());
        supervisorConsultationDao.updateById(sc);
    }

    private void responseSupervise(String sessionId){
        SupervisorConsultation sc = new SupervisorConsultation();
        sc.setRecordId(Integer.parseInt(sessionId));
        sc.setResponseTime(LocalDateTime.now());
        supervisorConsultationDao.updateById(sc);
    }

    @Transactional
    protected void saveChatLog(String sessionId, int sessionType) {
        String key = "chat:msg:list:"+sessionType+":"+sessionId;
        if(Boolean.FALSE.equals(redisTemplate.hasKey(key))){
            return ;
        }
        while(redisTemplate.opsForList().size(key)>0){
            ChatMsg chatMsg = JSON.parseObject(redisTemplate.opsForList().rightPop(key), ChatMsg.class);
            if(sessionType==1){
                chatLogDao.addRecordChatLog(Integer.parseInt(sessionId), chatMsg);
            }
            else{
                chatLogDao.addSessionChatLog(Integer.parseInt(sessionId), chatMsg);
            }
        }
        redisTemplate.delete(key);
    }

    @Transactional
    protected boolean isSessionCompleted(String sessionId){
        if( redisTemplate.hasKey(COMPLETED_SESSION_KEY) && redisTemplate.opsForSet().isMember(COMPLETED_SESSION_KEY, sessionId)){
            redisTemplate.expire(COMPLETED_SESSION_KEY, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            return true;
        }
        QueryWrapper<ConsultationSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", Integer.parseInt(sessionId));
        ConsultationSession cs =consultationSessionDao.selectOne(queryWrapper);
        if(cs==null || cs.getSessionStatus()==ConSessionStatus.completed || cs.getSessionStatus()==ConSessionStatus.COMPLETED) {
            redisTemplate.opsForSet().add(COMPLETED_SESSION_KEY, sessionId);
            redisTemplate.expire(COMPLETED_SESSION_KEY, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Transactional
    protected boolean isSupervisorCompleted(String sessionId){
        if( redisTemplate.hasKey(COMPLETED_SUPERVISOR_KEY) && redisTemplate.opsForSet().isMember(COMPLETED_SUPERVISOR_KEY, sessionId)){
            redisTemplate.expire(COMPLETED_SUPERVISOR_KEY, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            return true;
        }
        QueryWrapper<SupervisorConsultation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_id", Integer.parseInt(sessionId));
        SupervisorConsultation sc = supervisorConsultationDao.selectOne(queryWrapper);
        if(sc==null || sc.getResponseTime()!=null) {
            redisTemplate.opsForSet().add(COMPLETED_SUPERVISOR_KEY, sessionId);
            redisTemplate.expire(COMPLETED_SUPERVISOR_KEY, ChatConstant.SESSION_TIMEOUT, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }
}
