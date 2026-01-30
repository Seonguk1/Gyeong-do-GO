package com.project.gyeong_do_go.game.controller;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.request.JoinGameRequest;
import com.project.gyeong_do_go.game.dto.request.LocationRequest;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.LocationResponse;
import com.project.gyeong_do_go.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final SimpMessagingTemplate template;
    private final GameService gameService;

    @MessageMapping("/game/join")
    public void joinGame(@Payload JoinGameRequest request) {
        gameService.broadcastRoomInfo(request.getRoomId());
    }

    @MessageMapping("/game/location")
    public void sendLocation(LocationRequest request) {
        LocationResponse locationData = LocationResponse.builder()
                .playerId(request.getPlayerId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        GameResponse<LocationResponse> response = GameResponse.<LocationResponse>builder()
                .type(GameMessageType.UPDATE_LOCATION) // 타입 명시
                .data(locationData)
                .build();

        template.convertAndSend("/topic/room/" + request.getRoomId(), response);
    }



}