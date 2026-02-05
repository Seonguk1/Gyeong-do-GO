package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.CatchResponse;
import com.project.gyeong_do_go.game.dto.response.RescueResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.global.util.GeometryUtil;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.entity.PlayerRedis;
import com.project.gyeong_do_go.player.repository.PlayerRedisRepository;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameActionService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final GameFlowService gameFlowService;
    private final GameBroadcaster gameBroadcaster;
    private final GameReader  gameReader;
    private static final double CATCH_DISTANCE_LIMIT = 5.0;

    @Transactional
    public void updateLocation(Long playerId, double latitude, double longitude) {
        PlayerRedis playerRedis = playerRedisRepository.findById(playerId)
                .orElse(PlayerRedis.builder()
                        .playerId(playerId)
                        .latitude(latitude)
                        .longitude(longitude)
                        .totalDistance(0.0)
                        .build());

        playerRedis.updatePosition(latitude, longitude);
        playerRedisRepository.save(playerRedis);

        gameBroadcaster.broadcastLocation(playerId, latitude, longitude);

        // 검거 로직
    }

    @Transactional
    public void catchThief(Long policeId, String targetNumber) {
        Player police = gameRepository.getPlayer(policeId);
        Room room = police.getRoom();
        Player thief = playerRepository.findByRoomIdAndPrisonerNumber(room.getId(), targetNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 죄수 번호입니다."));

        validateCatchRequest(police, thief);

        // 거리 검증
        double distance = GeometryUtil.calculateDistance(police.getLatitude(), police.getLongitude(), thief.getLatitude(), thief.getLongitude());
        if (distance > CATCH_DISTANCE_LIMIT) {
            throw new IllegalArgumentException("거리가 너무 멀어 검거할 수 없습니다. 거리: " + (int)distance + "m");
        }

        thief.arrest();
        police.increaseCatchCount();
        thief.markAsCaught();

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

    @Transactional
    public void rescuePrisoners(Long rescuerId) {
        Player rescuer = gameReader.getPlayer(rescuerId);
        Room room = rescuer.getRoom();

        validateRescueCondition(rescuer, room);

        double distance = GeometryUtil.calculateDistance(
                rescuer.getLatitude(), rescuer.getLongitude(),
                room.getCenterLat(), room.getCenterLng()
        );
        if (distance > room.getPrisonRadius()) {
            throw new IllegalStateException("감옥과 너무 멉니다. 더 가까이 가세요!");
        }
        List<Player> prisoners = gameRepository.findPrisonersByRoomId(room.getId());
        if (prisoners.isEmpty()) {
            throw new IllegalStateException("구출할 동료가 없습니다.");
        }
        List<String> rescuedNicknames = new ArrayList<>();
        for (Player prisoner : prisoners) {
            prisoner.rescue();
            rescuedNicknames.add(prisoner.getNickname());
        }

        rescuer.increaseRescueCount(prisoners.size());

        RescueResponse response = RescueResponse.builder()
                .rescuerNickname(rescuer.getNickname())
                .rescuedCount(prisoners.size())
                .rescuedNicknames(rescuedNicknames)
                .build();

        gameBroadcaster.sendToRoom(room.getId(), GameMessageType.PLAYER_RESCUED, response);
    }
    private void validateRescueCondition(Player rescuer, Room room) {
        if (rescuer.getRole() != Role.THIEF) {
            throw new IllegalStateException("도둑만 탈옥을 시도할 수 있습니다.");
        }
        if (rescuer.getStatus() == PlayerStatus.OUT) {
            throw new IllegalStateException("죽은 자는 탈옥을 시도할 수 없습니다.");
        }
        // 게임 진행 중인지 확인 (RUNAWAY 때는 감옥 기능 비활성 등 규칙에 따라 추가)
        if (room.getRoomStatus() != GameStatus.PLAYING) {
            throw new IllegalStateException("게임 진행 중에만 가능합니다.");
        }
    }
}
