package com.project.gyeong_do_go.global.socket;

import com.project.gyeong_do_go.game.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final WebSocketSessionManager sessionManager;
    private final GameSessionService gameSessionService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        Long playerId = sessionManager.removeSession(sessionId);

        if (playerId != null) {
            log.info("사용자 연결 끊김 감지: sessionId={}, playerId={}", sessionId, playerId);
            gameSessionService.handleDisconnect(playerId);
        }
    }
}