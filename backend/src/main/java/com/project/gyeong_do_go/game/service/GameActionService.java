package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.CatchResponse;
import com.project.gyeong_do_go.game.dto.response.LocationResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.global.util.GeometryUtil;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameActionService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameFlowService gameFlowService;
    private final GameBroadcaster gameBroadcaster;
    private final GameReader  gameReader;
    private static final double CATCH_DISTANCE_LIMIT = 5.0;

    @Transactional
    public void updateLocation(Long playerId, double latitude, double longitude) {
        Player player = gameReader.getPlayer(playerId);
        Room room = player.getRoom();
        Long roomId = room.getId();
        if (room.getRoomStatus() != GameStatus.RUNAWAY && room.getRoomStatus() != GameStatus.PLAYING) {
            return;
        }
        // 위치 저장 (Redis 또는 DB/Memory)
        gameRepository.updatePlayerLocation(roomId, playerId, latitude, longitude);

        LocationResponse locationData = LocationResponse.builder()
                .playerId(playerId)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        gameBroadcaster.sendToRoom(roomId, GameMessageType.UPDATE_LOCATION, locationData);

        // 검거 로직
    }

    @Transactional
    public void catchThief(Long policeId, Long thiefId) {
        Player police = gameRepository.getPlayer(policeId);
        Player thief = gameRepository.getPlayer(thiefId);

        validateCatchRequest(police, thief);

        // 거리 검증
        double distance = GeometryUtil.calculateDistance(police.getLatitude(), police.getLongitude(), thief.getLatitude(), thief.getLongitude());
        if (distance > CATCH_DISTANCE_LIMIT) {
            throw new IllegalArgumentException("거리가 너무 멀어 검거할 수 없습니다. 거리: " + (int)distance + "m");
        }

        thief.updateStatus(PlayerStatus.OUT);

        Long roomId = police.getRoom().getId();

        CatchResponse response = CatchResponse.builder()
                .policeNickname(police.getNickname())
                .policeId(police.getId())
                .thiefNickname(thief.getNickname())
                .thiefId(thief.getId())
                .build();

        gameBroadcaster.sendToRoom(roomId, GameMessageType.PLAYER_CAUGHT, response);
        gameFlowService.checkGameOverCondition(roomId);
    }

    private void validateCatchRequest(Player police, Player thief) {
        if (!police.getRoom().getId().equals(thief.getRoom().getId())) { // 같은 방인가?
            throw new IllegalArgumentException("같은 방의 플레이어만 검거할 수 있습니다.");
        }
        if (police.getRole() != Role.POLICE || thief.getRole() != Role.THIEF) { // 역할이 맞는가?
            throw new IllegalArgumentException("잘못된 역할입니다.");
        }
        if (thief.getStatus() == PlayerStatus.OUT) { // 도둑이 이미 잡힌 상태인가?
            // 동시 클릭 등으로 이미 잡혔다면 조용히 리턴하거나 에러
            throw new IllegalArgumentException("이미 검거된 도둑입니다.");
        }
        GameStatus status = police.getRoom().getRoomStatus();
        if (status != GameStatus.PLAYING && status != GameStatus.RUNAWAY) { // 게임 진행 중인가?
            throw new IllegalArgumentException("게임 진행 중에만 검거할 수 있습니다.");
        }
    }
}
