package com.project.gyeong_do_go.global.socket;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final Map<String, Long> sessionMap = new ConcurrentHashMap<>();

    public void registerSession(String sessionId, Long playerId) {
        sessionMap.put(sessionId, playerId);
    }

    public Long getPlayerId(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public Long removeSession(String sessionId) {
        return sessionMap.remove(sessionId);
    }
}