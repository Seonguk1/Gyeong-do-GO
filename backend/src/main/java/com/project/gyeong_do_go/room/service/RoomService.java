package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.domain.Role;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.PlayerRepository;
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

        roomRepository.save(room);

        Player host = Player.builder()
                .room(room)
                .nickname(request.nickname())
                .isHost(true)
                .build();

        room.addPlayer(host);

        playerRepository.save(host);

        return room;
    }

    @Transactional
    public Player joinRoom(String roomCode, String nickname) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        if (room.getGameStatus() != GameStatus.WAITING) {
            throw new CustomException(ErrorCode.GAME_ALREADY_STARTED);
        }

        Player player = Player.builder()
                .room(room)
                .nickname(nickname)
                .isHost(false)
                .build();

        return playerRepository.save(player);
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

        room.startGame();
    }

    @Transactional
    public void updatePlayerRole(Long playerId, Role newRole) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));
        player.updateRole(newRole);
    }

    @Transactional
    public void updatePlayerReady(Long playerId, boolean isReady) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));
        player.toggleReady(isReady);
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}