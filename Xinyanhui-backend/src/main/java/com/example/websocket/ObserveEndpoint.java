package com.example.websocket;

import com.example.config.ObserveWebsocketConfig;
import com.example.constants.TypeConstant;
import com.example.service.ObserveService;
import com.example.utils.JwtUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ServerEndpoint(value = "/observe", configurator = ObserveWebsocketConfig.class)
@Slf4j
public class ObserveEndpoint {

    private final ObserveService observeService;

    @Autowired
    public ObserveEndpoint(ObserveService observeService) {
        this.observeService = observeService;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //get token and sessionId from url
        String token = (String) config.getUserProperties().get("token");
        Integer sessionId = Integer.parseInt((String) config.getUserProperties().get("sessionId") );
        session.getUserProperties().put("sessionId",sessionId);
        // 从 token 参数中获取id和type
        Map<String, Object> params = JwtUtil.parseJwt(token);
        Integer Id = (Integer) params.get("id");
        String userType = (String) params.get("type");
        if (Id == null || sessionId == null) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "缺少参数"));
            } catch (Exception ignored) {}
            return;
        }
        else if(!TypeConstant.SUPERVISOR_STR.equals(userType)){
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "身份不合法"));
            } catch (Exception ignored) {}
        }
        observeService.addObservedSession(sessionId, session);
        log.info("[OPEN] 新连接 sessionId={}, session={}", sessionId,session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        //get token and sessionId from url
        Integer sessionId = (Integer)session.getUserProperties().get("sessionId") ;
        observeService.removeObservedSession(sessionId);
        log.info("[CLOSE] 断开连接 sessionId={}, session={}", sessionId,session.getId());
    }

}
