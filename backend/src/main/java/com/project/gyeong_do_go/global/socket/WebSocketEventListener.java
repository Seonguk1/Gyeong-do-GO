package com.project.gyeong_do_go.global.socket;

import com.project.gyeong_do_go.game.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final GameSessionService gameSessionService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 1. 이벤트 메시지에서 헤더 접근자(Accessor) 래핑
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // 2. 세션 속성에서 값 꺼내기 (Interceptor에서 저장한 값)
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");

        // 3. 값이 존재하면 퇴장 처리
        if (roomId != null && playerId != null) {
            log.info("사용자 연결 끊김 감지: roomId={}, playerId={}", roomId, playerId);
            // 기존 handleDisconnect가 roomId도 필요하다면 같이 넘겨주는 게 좋음
            gameSessionService.handleDisconnect(roomId, playerId);
        }
    }
}