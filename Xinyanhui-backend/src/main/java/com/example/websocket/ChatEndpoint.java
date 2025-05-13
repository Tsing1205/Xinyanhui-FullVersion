package com.example.websocket;

import com.example.config.ChatWebSocketConfigurator;
import com.example.service.ChatService;
import com.example.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/chat", configurator = ChatWebSocketConfigurator.class)
@Component
@Slf4j
public class ChatEndpoint {

    private String userType;
    private int Id;
    private String sessionId;
    int sessionType;
    private ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    //用于检查是否创建了新实例
    @PostConstruct
    public void init() {
        log.info("ChatEndpoint 实例被创建：{}" , this);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws Exception {
        //get token from url
        String token = (String)  config.getUserProperties().get("token");

        // 从 token 参数中获取用户名

        Map<String, Object> params = JwtUtil.parseJwt(token);
        this.Id = (Integer) params.get("id");
        this.userType = (String) params.get("type");
        this.sessionId = (String) config.getUserProperties().get("sessionId");
        this.sessionType = Integer.parseInt((String) config.getUserProperties().get("type"));

        if (sessionId == null || sessionId.isEmpty() || userType==null || Id==0  ) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Username is required"));
            return;
        }
        log.info("[OPEN] 新连接 logicSessionId={}, userId={},sessionId={}", sessionId, Id,session.getId());
        chatService.addSession(sessionId, Id, userType, sessionType, session);
    }

    @OnMessage
    public void onMessage(String message) {
        chatService.sendMessage(sessionId, Id, userType, sessionType, message);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        log.info("[CLOSE] 连接关闭 sessionId={}, userId={}", sessionId, Id);
        if(reason.getCloseCode()== CloseReason.CloseCodes.CANNOT_ACCEPT){
            return ;
        }
        else if(reason.getCloseCode().getCode()==4001){
            chatService.removeAbnormalSession(sessionId);
        }
        chatService.removeSession(sessionId, Id, userType, sessionType);
    }

}
