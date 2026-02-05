package com.project.gyeong_do_go.global.exception;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.socket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class SocketExceptionHandler {

    private final SimpMessagingTemplate template;
    private final WebSocketSessionManager sessionManager; // 세션에서 ID 꺼내기 위해

    // CustomException이 터지면 이 메서드가 납치함
    @MessageExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException e, SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();
        Long playerId = sessionManager.getPlayerId(sessionId);

        ApiResponse<Void> errorResponse = ApiResponse.fail(e.getErrorCode(), e.getErrorCode().getMessage());

        template.convertAndSend("/queue/player/" + playerId, errorResponse);
    }
}