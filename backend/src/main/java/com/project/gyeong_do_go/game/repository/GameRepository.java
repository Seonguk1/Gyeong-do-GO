package com.project.gyeong_do_go.game.repository;

import com.project.gyeong_do_go.game.dto.response.UpdateRoomResponse;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.PlayerRepository;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameRepository {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    public UpdateRoomResponse getRoomFullData(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        List<Player> players = playerRepository.findByRoomId(roomId);

        List<UpdateRoomResponse.PlayerInfo> playerDtos = players.stream()
                .map(p -> UpdateRoomResponse.PlayerInfo.builder()
                        .id(p.getId())
                        .nickname(p.getNickname())
                        .role(p.getRole().name())
                        .isReady(p.isReady())
                        .build())
                .collect(Collectors.toList());

        return UpdateRoomResponse.builder()
                .roomId(room.getId())
                .roomCode(room.getRoomCode())
                .roomStatus(room.getGameStatus().name())
                .centerLat(room.getCenterLat())
                .centerLon(room.getCenterLon())
                .mapRadius(room.getMapRadius())
                .prisonRadius(room.getPrisonRadius())
                .timeLimit(room.getTimeLimit())
                .runawayLimit(room.getRunawayLimit())
                .players(playerDtos)
                .build();
    }
}