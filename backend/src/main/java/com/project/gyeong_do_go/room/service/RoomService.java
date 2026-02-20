package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameValidator;
import com.project.gyeong_do_go.global.entity.RoomAndPlayer;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRedisRepository;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.dto.request.RoomSettingRequest;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final GameBroadcaster gameBroadcaster;
    private final GameValidator gameValidator;

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public Room createRoom(CreateRoomRequest request) {
        String roomCode;
        do {
            roomCode = generateRandomCode();
        } while (roomRepository.findByRoomCode(roomCode).isPresent());

        Room room = Room.builder()
                .roomCode(roomCode)
                .centerLat(request.latitude())
                .centerLon(request.longitude())
                .mapRadius(request.mapRadius())
                .prisonRadius(request.prisonRadius())
                .timeLimit(request.timeLimit())
                .runawayLimit(request.runawayLimit())
                .build();

        Player host = Player.builder()
                .room(room)
                .nickname(request.nickname())
                .isHost(true)
                .build();

        host.setReady(true);
        room.addPlayer(host);

        roomRepository.save(room);
        playerRepository.save(host);

        return room;
    }

    @Transactional
    public Player joinRoom(String roomCode, String nickname) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        if (room.getRoomStatus() != GameStatus.WAITING) {
            throw new CustomException(ErrorCode.ROOM_NOT_JOINABLE);
        }

        if (playerRepository.existsByRoomAndNickname(room, nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }

        Player player = Player.builder()
                .room(room)
                .nickname(nickname)
                .isHost(false)
                .build();

        return playerRepository.save(player);
    }

    @Transactional
    public void updateRoomSettings(Long roomId, RoomSettingRequest req) {
        RoomAndPlayer roomAndPlayer = gameValidator.validateAndGet(roomId, req.playerId());
        Room room = roomAndPlayer.room(); Player player = roomAndPlayer.player();

        if (!player.isHost()) throw new CustomException(ErrorCode.NOT_HOST);

        if (room.getRoomStatus() != GameStatus.WAITING) throw new CustomException(ErrorCode.GAME_ALREADY_STARTED);

        // 방장 권한 체크 (필수)
        // (실무에선 SecurityContext에서 꺼내온 ID와 room.getHostId() 비교)

        room.updateSettings(
                req.centerLat(), req.centerLng(),
                req.mapRadius(), req.prisonRadius(),
                req.timeLimit(), req.runawayLimit()
        );

        gameBroadcaster.broadcastRoomInfo(roomId);
    }

    public Room getRoomDetail(Long roomId) {
        return roomRepository.findByIdWithPlayers(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Transactional
    public void resetToLobby(Long playerId) {
        Player host = playerRepository.findById(playerId).get();
        if (!host.isHost()) {
            throw new CustomException(ErrorCode.NOT_HOST);
        }

        Room room = host.getRoom();
        Long roomId = room.getId();

        if (room.getRoomStatus() != GameStatus.FINISHED) {
            throw new IllegalStateException("게임이 종료된 상태에서만 재시작 가능합니다.");
        }
        room.updateStatus(GameStatus.WAITING);
        room.setStartedAt(null); // 시작 시간 초기화

        List<Player> players = room.getPlayers();
        List<Long> playerIds = new ArrayList<>(); // Redis 삭제용 ID 모음

        for (Player p : players) {
            p.setReady(false);
            p.setStatus(PlayerStatus.ALIVE);
            p.updateLocation(0.0, 0.0);
            p.setCaughtAt(null);
            p.setPrisonerNumber(null);
            playerIds.add(p.getId());
        }

        playerRedisRepository.deleteAllById(playerIds);

        // 만약 쿨타임 등을 별도 키로 관리했다면 그것도 삭제
        // redisTemplate.delete("catch_cooldown:" + playerId); ...

        gameBroadcaster.broadcastRoomInfo(roomId);
    }
}