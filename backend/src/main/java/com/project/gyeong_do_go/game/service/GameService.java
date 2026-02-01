package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.request.LocationRequest;
import com.project.gyeong_do_go.game.dto.response.*;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.domain.PlayerStatus;
import com.project.gyeong_do_go.room.domain.Role;
import com.project.gyeong_do_go.room.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

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

        taskScheduler.schedule(() -> timeOver(roomId), finishTime);
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

    @Transactional
    public void handleLocationUpdate(LocationRequest req){
        Long roomId = req.getRoomId();

        GameStatus status = gameRepository.getRoomStatus(roomId);
        if (status != GameStatus.PLAYING && status != GameStatus.RUNAWAY) {
            return;
        }

        // 2. 위치 저장 (Redis 또는 DB/Memory)
        gameRepository.updatePlayerLocation(roomId, req.getPlayerId(), req.getLatitude(), req.getLongitude());

        LocationResponse locationRes = LocationResponse.builder()
                .playerId(req.getPlayerId())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .build();
        sendToRoom(roomId, GameMessageType.UPDATE_LOCATION, locationRes);

        // 검거 로직
    }

    private void sendToRoom(Long roomId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/topic/room/" + roomId, response);
    }

    private void processCatch(Long roomId, Player police, Player thief) {
        gameRepository.updatePlayerStatus(thief.getId(), PlayerStatus.JAILED);

        CatchResponse catchData = CatchResponse.builder()
                .policeId(police.getId())
                .policeNickname(police.getNickname())
                .thiefId(thief.getId())
                .thiefNickname(thief.getNickname())
                .build();
        sendToRoom(roomId, GameMessageType.PLAYER_CAUGHT, catchData);

        // 게임 종료 조건 확인 (도둑이 전멸했는가?)
        checkGameOverCondition(roomId);
    }

    private void checkGameOverCondition(Long roomId) {
        List<Player> aliveThieves = gameRepository.getAliveThieves(roomId);

        if (aliveThieves.isEmpty()) {
            finishGame(roomId, Role.POLICE);
        }
    }

    private void finishGame(Long roomId, Role winnerTeam) {
        gameRepository.updateRoomStatus(roomId, "FINISHED");

        // 2. 결과 전송
        GameResultResponse result = GameResultResponse.builder()
                .winnerTeam(winnerTeam)
                // .mvpPlayer(...) 등 추가 가능
                .build();

        sendToRoom(roomId, GameMessageType.GAME_OVER, result);

        // TODO: 스케줄러가 돌아가고 있다면 취소해야 하는데,
        // 상태가 FINISHED로 바뀌었으므로 startMainGame() 등에서 status 체크를 하면
        // 굳이 취소 안 해도 무시되도록 짤 수 있음.
    }

    public void timeOver(Long roomId) {
        GameStatus status = gameRepository.getRoomStatus(roomId);
        if (status == GameStatus.FINISHED) return; // 이미 경찰이 이겨서 끝났으면 무시

        finishGame(roomId, Role.THIEF);
    }
}
