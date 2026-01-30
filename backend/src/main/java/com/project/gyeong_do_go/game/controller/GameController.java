package com.project.gyeong_do_go.game.controller;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.request.BaseGameRequest;
import com.project.gyeong_do_go.game.dto.request.JoinGameRequest;
import com.project.gyeong_do_go.game.dto.request.LocationRequest;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @MessageMapping("/game/join")
    public void joinGame(@Payload JoinGameRequest request) {
        gameService.broadcastRoomInfo(request.getRoomId());
    }

//    @MessageMapping("/game/{roomId}/location")
//    public void sendLocation(LocationRequest message) {
//        message.setType(GameMessageType.SEND_LOCATION);
//        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
//    }


}