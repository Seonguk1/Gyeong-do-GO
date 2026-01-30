package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SimpMessagingTemplate template;
    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public void broadcastRoomInfo(Long roomId) {
        UpdateRoomResponse roomData = gameRepository.getRoomFullData(roomId);

        sendToRoom(roomId, GameMessageType.UPDATE_ROOM, roomData);
    }

//    public void handleLocationUpdate(LocationRequest req){
//        Long roomId = req.getRoomId();
//        Long playerId = req.getPlayerId();
//
//        gameRepository.updatePlayerLocation(roomId, playerId, req.getLat(), req.getLng());
//
//        UpdateRoomResponse.PlayerInfo me = gameRepository.getPlayerInfo(roomId, playerId);
//        if (me == null) return; // 예외 처리
//    }

    private void sendToRoom(Long roomId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/topic/room/" + roomId, response);
    }
}
