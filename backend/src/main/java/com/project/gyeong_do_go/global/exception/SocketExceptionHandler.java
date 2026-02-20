package com.project.gyeong_do_go.global.exception;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class SocketExceptionHandler {

    private final SimpMessagingTemplate template;

    // CustomException이 터지면 이 메서드가 처리
    @MessageExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException e, SimpMessageHeaderAccessor accessor) { // 이름 accessor로 변경

        // 1. 세션에서 playerId 꺼내기
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");

        // 예외 발생 시점이나 종류에 따라 playerId가 없을 수도 있음 (연결 전 에러 등)
        if (playerId == null) {
            log.error("식별할 수 없는 사용자의 에러: {}", e.getMessage());
            return;
        }

        // 2. 에러 응답 생성
        ApiResponse<Void> errorResponse = ApiResponse.fail(e.getErrorCode(), e.getErrorCode().getMessage());

        log.warn("소켓 예외 발생: playerId={}, code={}, msg={}", playerId, e.getErrorCode(), e.getMessage());

        // 3. 해당 사용자에게 전송
        template.convertAndSend("/queue/player/" + playerId, errorResponse);
    }
}