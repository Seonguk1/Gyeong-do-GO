package com.project.gyeong_do_go.game.controller;

import com.project.gyeong_do_go.game.dto.request.CatchRequest;
import com.project.gyeong_do_go.game.dto.request.LocationRequest;
import com.project.gyeong_do_go.game.service.GameActionService;
import com.project.gyeong_do_go.game.service.GameFlowService;
import com.project.gyeong_do_go.game.service.GameSessionService;
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

    @MessageMapping("/game/join")
    public void joinGame(SimpMessageHeaderAccessor accessor) {
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");
        // 만약 세션에 값이 없다면? (인터셉터에서 걸러지겠지만, 방어 로직)
//        if (roomId == null || playerId == null) throw new CustomException(ErrorCode.UNAUTHORIZED);
        sessionService.joinGame(roomId, playerId);
    }

    @MessageMapping("/game/leave")
    public void leaveGame(SimpMessageHeaderAccessor accessor) {
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");
        if (playerId == null) return;
        sessionService.leaveGame(roomId, playerId);
    }

    @MessageMapping("/game/location")
    public void sendLocation(@Payload LocationRequest request, SimpMessageHeaderAccessor accessor) {
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");
        actionService.updateLocation(roomId, playerId, request.getLatitude(), request.getLongitude());
    }

    @MessageMapping("/game/catch")
    public void catchThief(@Payload CatchRequest request, SimpMessageHeaderAccessor accessor) {
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long policeId = (Long) accessor.getSessionAttributes().get("playerId");
        actionService.catchThief(roomId, policeId, request.getTargetNumber());
    }

    @MessageMapping("/game/rescue")
    public void rescuePrisoners(SimpMessageHeaderAccessor accessor) {
        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
        Long playerId = (Long) accessor.getSessionAttributes().get("playerId");
        actionService.rescuePrisoners(roomId, playerId);
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