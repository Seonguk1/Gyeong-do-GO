package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
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

    private void sendToRoom(Long roomId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/topic/room/" + roomId, response);
    }
}
