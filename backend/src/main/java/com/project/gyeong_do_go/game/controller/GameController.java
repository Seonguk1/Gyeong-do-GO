package com.project.gyeong_do_go.game.controller;

import com.project.gyeong_do_go.game.dto.request.CatchRequest;
import com.project.gyeong_do_go.game.dto.request.JoinGameRequest;
import com.project.gyeong_do_go.game.dto.request.LocationRequest;
import com.project.gyeong_do_go.game.service.GameActionService;
import com.project.gyeong_do_go.game.service.GameFlowService;
import com.project.gyeong_do_go.game.service.GameSessionService;
import com.project.gyeong_do_go.global.socket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameSessionService sessionService;
    private final GameFlowService flowService;
    private final GameActionService actionService;
    private final SimpMessagingTemplate template;
    private final WebSocketSessionManager sessionManager;

    @MessageMapping("/game/join")
    public void joinGame(@Payload JoinGameRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        sessionManager.registerSession(sessionId, request.getPlayerId());
        sessionService.joinGame(request.getPlayerId());
    }

    @MessageMapping("/game/leave")
    public void leaveGame(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Long playerId = sessionManager.getPlayerId(sessionId);
        if (playerId == null) return;
        sessionService.leaveGame(playerId);
        sessionManager.removeSession(sessionId);
    }

    @MessageMapping("/game/location")
    public void sendLocation(LocationRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Long playerId = sessionManager.getPlayerId(sessionId);
        actionService.updateLocation(playerId, request.getLatitude(), request.getLongitude());
    }

    @MessageMapping("/game/catch")
    public void catchThief(@Payload CatchRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Long policeId = sessionManager.getPlayerId(sessionId);
        actionService.catchThief(policeId, request.getTargetNumber());
    }

    @MessageMapping("/game/rescue")
    public void rescuePrisoners(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Long playerId = sessionManager.getPlayerId(sessionId);
        actionService.rescuePrisoners(playerId);
    }

//    @MessageExceptionHandler(MethodArgumentNotValidException.class)
//    @SendToUser("/queue/errors")
//    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
//
//        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
//
//        return ErrorResponse.builder()
//                .type("ERROR")
//                .code("INVALID_INPUT")
//                .message(errorMessage)
//                .build();
//    }
//
//    // 그 외 런타임 예외 처리
//    @MessageExceptionHandler(IllegalStateException.class)
//    @SendToUser("/queue/errors")
//    public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
//        return ErrorResponse.builder()
//                .type("ERROR")
//                .code("GAME_ERROR")
//                .message(ex.getMessage())
//                .build();
//    }

}