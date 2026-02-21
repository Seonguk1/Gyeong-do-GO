package com.project.gyeong_do_go.game.repository;

import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GameRepository{

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    // ==========================================
    //  조회 로직 (Read) - @Transactional(readOnly = true)
    // ==========================================

    @Transactional(readOnly = true)
    public UpdateRoomResponse getRoomFullData(Long roomId) {
        Room room = findRoomById(roomId);
        List<Player> players = playerRepository.findByRoomId(roomId);

        return UpdateRoomResponse.builder()
                .roomId(room.getId())
                .roomCode(room.getRoomCode())
                .roomStatus(room.getRoomStatus().name())
                .centerLat(room.getCenterLat())
                .centerLon(room.getCenterLng())
                .mapRadius(room.getMapRadius())
                .prisonRadius(room.getPrisonRadius())
                .timeLimit(room.getTimeLimit())
                .runawayLimit(room.getRunawayLimit())
                .players(toPlayerDtos(players))
                .build();
    }

    @Transactional(readOnly = true)
    public List<Player> getPlayers(Long roomId) {
        return playerRepository.findByRoomId(roomId);
    }

    @Transactional(readOnly = true)
    public int getRunawayLimit(Long roomId) {
        return findRoomById(roomId).getRunawayLimit();
    }

    @Transactional(readOnly = true)
    public int getTimeLimit(Long roomId) {
        return findRoomById(roomId).getTimeLimit();
    }

    @Transactional(readOnly = true)
    public int getPrisonLimit(Long roomId) { return findRoomById(roomId).getPrisonRadius(); }

    @Transactional(readOnly = true)
    public int getMapRadius(Long roomId) { return findRoomById(roomId).getMapRadius(); }

    @Transactional(readOnly = true)
    public GameStatus getRoomStatus(Long roomId) { return findRoomById(roomId).getRoomStatus(); }

    @Transactional(readOnly = true)
    public Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("플레이어를 찾을 수 없습니다. id=" + playerId));
    }

    @Transactional(readOnly = true)
    public List<Player> getAliveThieves(Long roomId) {
        // (성능 최적화를 위해선 JPQL로 "SELECT p FROM Player p WHERE p.room.id = :roomId AND p.role = 'THIEF' AND p.status != 'OUT'" 쿼리를 만드는 게 좋음)
        return playerRepository.findByRoomId(roomId).stream()
                .filter(p -> p.getRole() == Role.THIEF)
                .filter(p -> PlayerStatus.ALIVE.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Player> findPrisonersByRoomId(Long roomId) {
        return playerRepository.findPrisonersByRoomId(roomId);
    }

    @Transactional(readOnly = true)
    public List<Room> getPlayingRooms() {
        return roomRepository.findByRoomStatus(GameStatus.PLAYING);
    }

    // ==========================================
    //  변경 로직 (Write) - @Transactional 필수
    // ==========================================

    @Transactional
    public void updateRoomStatus(Long roomId, String statusName) {
        Room room = findRoomById(roomId);
        GameStatus status = GameStatus.valueOf(statusName); // String -> Enum 변환

        // 엔터티의 비즈니스 로직 메서드를 호출 (Dirty Checking으로 자동 저장됨)
        switch (status) {
            case ROLE_CHECK -> room.startRoleCheck();
            case STARTING -> room.startStarting();
            case RUNAWAY -> room.startRunaway();
            case PLAYING -> room.startMainGame();
            case FINISHED -> room.finishGame();
            default -> throw new IllegalArgumentException("유효하지 않은 게임 상태입니다: " + statusName);
        }
    }

    @Transactional
    public void updatePlayerStatus(Long playerId, PlayerStatus status) {
        Player player = getPlayer(playerId);

        player.setStatus(status);
    }

    @Transactional
    public void saveAll(List<Player> players) {
        playerRepository.saveAll(players);
    }

    public void deleteOldRooms(LocalDateTime standardTime) {
        roomRepository.deleteByCreatedAtBefore(standardTime);
        // 여기서 방송하지 마세요! 삭제만 하세요.
    }

    // ==========================================
    //  내부 헬퍼 메서드
    // ==========================================

    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다. roomId=" + roomId));
    }

    private List<UpdateRoomResponse.PlayerInfo> toPlayerDtos(List<Player> players) {
        return players.stream()
                .map(p -> UpdateRoomResponse.PlayerInfo.builder()
                        .id(p.getId())
                        .nickname(p.getNickname())
                        .role(p.getRole() != null ? p.getRole().name() : "NONE")
                        .isHost(p.isHost())
                        .isReady(p.isReady())
                        .build())
                .collect(Collectors.toList());
    }
}