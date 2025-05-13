package com.example.config;

import com.example.utils.SpringContextUtil;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class NotificationWebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return SpringContextUtil.getBean(clazz);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        // 读取请求参数
        Map<String, List<String>> parameterMap = request.getParameterMap();

        if (parameterMap.containsKey("token")) {
            String token = parameterMap.get("token").get(0);
            sec.getUserProperties().put("token", token);
        }
    }
}
