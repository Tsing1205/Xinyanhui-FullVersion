package com.example.websocket;

import com.example.config.NotificationWebSocketConfig;
import com.example.service.NotifyService;
import com.example.utils.JwtUtil;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@ServerEndpoint(value = "/notify", configurator = NotificationWebSocketConfig.class)
@Slf4j
public class NotifyEndpoint {

    private final NotifyService notifyService;

    @Autowired
    public NotifyEndpoint(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        //get token from url
        String token = (String) config.getUserProperties().get("token");
        // 从 token 参数中获取id和type
        Map<String, Object> params = JwtUtil.parseJwt(token);
        Integer Id = (Integer) params.get("id");
        String userType = (String) params.get("type");

        if (Id == null ) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing userId"));
            } catch (Exception ignored) {}
            return;
        }

        notifyService.addSession(Id, userType, session);
        log.info("[OPEN] 新连接 sessionId={}, userId={}", session.getId(), Id);
    }

    public void onClose(Session session, EndpointConfig config){
        //get token from url
        String token = (String) config.getUserProperties().get("token");
        // 从 token 参数中获取id和type
        Map<String, Object> params = JwtUtil.parseJwt(token);
        Integer Id = (Integer) params.get("id");
        String userType = (String) params.get("type");

        notifyService.removeSession(Id,userType);
        log.info("[CLOSE] 断开连接 sessionId={}", session.getId());
    }
}
