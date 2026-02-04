package com.project.gyeong_do_go.game.component;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.GameStartResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class GameBroadcaster {
    private final SimpMessagingTemplate template;
    private final GameRepository gameRepository;

    public void sendToRoom(Long roomId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/topic/room/" + roomId, response);
    }

    @Transactional(readOnly = true)
    public void broadcastRoomInfo(Long roomId) {
        UpdateRoomResponse roomData = gameRepository.getRoomFullData(roomId);
        sendToRoom(roomId, GameMessageType.UPDATE_ROOM, roomData);
    }

    public void broadcastPhase(Long roomId, String roomStatus, Instant finishTime) {
        // 플레이어 리스트가 필요하면 여기서 조회해서 DTO로 변환
        // (게임 진행 중에는 플레이어 목록을 매번 보낼 필요가 없다면 생략 가능)

        GameStartResponse res = GameStartResponse.builder()
                .roomStatus(roomStatus)
                .finishTime(finishTime.toString())
                .build();

        sendToRoom(roomId, GameMessageType.ROOM_STATUS_CHANGE, res);
    }

    public void broadcastCatch(Long roomId, String policeName, String thiefName) {
        // ... 검거 메시지 조립 후 sendToRoom 호출
    }
}
