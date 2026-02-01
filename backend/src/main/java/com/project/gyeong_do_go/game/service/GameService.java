package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.GameStartResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SimpMessagingTemplate template;
    private final GameRepository gameRepository;
    private final TaskScheduler taskScheduler;

    @Transactional(readOnly = true)
    public void broadcastRoomInfo(Long roomId) {
        UpdateRoomResponse roomData = gameRepository.getRoomFullData(roomId);

        sendToRoom(roomId, GameMessageType.UPDATE_ROOM, roomData);
    }

    @Transactional
    public void startGame(Long roomId, Long requesterId) {
        gameRepository.updateRoomStatus(roomId, "ROLE_CHECK");

        Instant finishTime = Instant.now().plusSeconds(10);

        broadcastPhase(roomId, "ROLE_CHECK", finishTime);

        taskScheduler.schedule(() -> startRunawayPhase(roomId), finishTime);
    }

    @Transactional
    public void startRunawayPhase(Long roomId) {
        int runawayLimit = gameRepository.getRunawayLimit(roomId);

        gameRepository.updateRoomStatus(roomId, "RUNAWAY");

        Instant finishTime = Instant.now().plusSeconds(runawayLimit);

        broadcastPhase(roomId, "RUNAWAY", finishTime);

        taskScheduler.schedule(() -> startMainGame(roomId), finishTime);
    }

    @Transactional
    public void startMainGame(Long roomId) {
        int timeLimit = gameRepository.getTimeLimit(roomId); // 예: 600초

        gameRepository.updateRoomStatus(roomId, "PLAYING");

        Instant finishTime = Instant.now().plusSeconds(timeLimit);

        // 메인 게임은 다음 스케줄(게임 종료)이 필요하다면 여기에 추가
        broadcastPhase(roomId, "PLAYING", finishTime);
    }

    private void broadcastPhase(Long roomId, String roomStatus, Instant finishTime) {
        // 플레이어 리스트가 필요하면 여기서 조회해서 DTO로 변환
        // (게임 진행 중에는 플레이어 목록을 매번 보낼 필요가 없다면 생략 가능)

        GameStartResponse res = GameStartResponse.builder()
                .roomStatus(roomStatus)
                .finishTime(finishTime.toString())
                .build();

        sendToRoom(roomId, GameMessageType.ROOM_STATUS_CHANGE, res);
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
