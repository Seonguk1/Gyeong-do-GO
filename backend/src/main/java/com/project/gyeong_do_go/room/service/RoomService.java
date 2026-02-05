package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.entity.Player;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final GameBroadcaster gameBroadcaster;

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
            throw new CustomException(ErrorCode.GAME_ALREADY_STARTED);
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
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        // 방장 권한 체크 (필수)
        // (실무에선 SecurityContext에서 꺼내온 ID와 room.getHostId() 비교)

        if (room.getRoomStatus() != GameStatus.WAITING) {
            throw new IllegalStateException("게임 대기 중에만 설정을 변경할 수 있습니다.");
        }

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

    @Transactional
    public void startGame(Long roomId, Long playerId) {
        Room room = getRoomDetail(roomId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));

        if (!player.isHost()) {
            throw new CustomException(ErrorCode.NOT_HOST);
        }

        room.startRoleCheck();
    }



    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}