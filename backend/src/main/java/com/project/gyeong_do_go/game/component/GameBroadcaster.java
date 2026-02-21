package com.project.gyeong_do_go.game.component;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResponse;
import com.project.gyeong_do_go.game.dto.response.GameStartResponse;
import com.project.gyeong_do_go.game.dto.response.LocationResponse;
import com.project.gyeong_do_go.game.dto.response.LocationSnapshotResponse;
import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.entity.PlayerRedis;
import com.project.gyeong_do_go.player.repository.PlayerRedisRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameBroadcaster {
    private final SimpMessagingTemplate template;
    private final GameRepository gameRepository;
    private final GameReader gameReader;
    private final PlayerRedisRepository playerRedisRepository;

    public void sendToRoom(Long roomId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/topic/room/" + roomId, response);
    }

    public void sendToPlayer(Long playerId, GameMessageType type, Object data) {
        GameResponse<Object> response = GameResponse.builder()
                .type(type)
                .data(data)
                .build();
        template.convertAndSend("/queue/player/" + playerId, response);
    }

    @Transactional(readOnly = true)
    public void broadcastRoomInfo(Long roomId) {
        UpdateRoomResponse roomData = gameRepository.getRoomFullData(roomId);
        sendToRoom(roomId, GameMessageType.UPDATE_ROOM, roomData);
    }

    public void broadcastPhase(Long roomId, GameStatus roomStatus, Instant finishTime) {
        // 플레이어 리스트가 필요하면 여기서 조회해서 DTO로 변환
        // (게임 진행 중에는 플레이어 목록을 매번 보낼 필요가 없다면 생략 가능)

        GameStartResponse res = GameStartResponse.builder()
                .roomStatus(roomStatus)
                .finishTime(finishTime.toString())
                .build();

        sendToRoom(roomId, GameMessageType.ROOM_STATUS_CHANGE, res);

        if (roomStatus == GameStatus.ROLE_CHECK) {
            sendSecretNumbersToThieves(roomId);
        }
    }

    // 귓속말 전용 메서드
    private void sendSecretNumbersToThieves(Long roomId) {
        List<Player> players = gameReader.getPlayersInRoom(roomId);
        for (Player p : players) {
            if (p.getRole() == Role.THIEF) {
                String prisonerNum = p.getPrisonerNumber();
                sendToPlayer(p.getId(), GameMessageType.PRISONER_NUMBER, prisonerNum);
            }
        }
    }

    public void broadcastLocation(Long playerId, double latitude, double longitude) {
        Player player = gameReader.getPlayer(playerId);
        Long roomId = player.getRoom().getId();
        LocationResponse locationData = LocationResponse.builder()
                .playerId(playerId)
                .latitude(latitude)
                .longitude(longitude)
                .build();

            sendToRoom(roomId, GameMessageType.UPDATE_LOCATION, locationData);
    }

    @Transactional(readOnly = true)
    public void broadcastLocationSnapshot(Long roomId) {
        List<Player> players = gameRepository.getPlayers(roomId);
        List<LocationResponse> locations = players.stream()
                .map(player -> {
                    PlayerRedis playerRedis = playerRedisRepository.findById(player.getId())
                            .orElse(null);
                    double lat = playerRedis != null ? playerRedis.getLatitude() : player.getLatitude();
                    double lng = playerRedis != null ? playerRedis.getLongitude() : player.getLongitude();
                    return LocationResponse.builder()
                            .playerId(player.getId())
                            .latitude(lat)
                            .longitude(lng)
                            .build();
                })
                .collect(Collectors.toList());

        LocationSnapshotResponse snapshot = LocationSnapshotResponse.builder()
                .locations(locations)
                .timestamp(System.currentTimeMillis())
                .build();

        sendToRoom(roomId, GameMessageType.UPDATE_LOCATIONS, snapshot);
    }
}
