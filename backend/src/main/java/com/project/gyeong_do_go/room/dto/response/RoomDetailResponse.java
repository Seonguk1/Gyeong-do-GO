package com.project.gyeong_do_go.room.dto.response;

import com.project.gyeong_do_go.room.domain.Role;
import com.project.gyeong_do_go.room.entity.Room;

import java.util.List;

public record RoomDetailResponse(
        Long roomId,
        String roomCode,
        String roomStatus,
        Long myId,
        List<PlayerDetail> players
) {
    public record PlayerDetail(
            Long id,
            String nickname,
            Role role,
            boolean isHost,
            boolean isReady
    ) {}

    public static RoomDetailResponse from(Room room, Long myId) {
        return new RoomDetailResponse(
                room.getId(),
                room.getRoomCode(),
                room.getGameStatus().name(),
                myId,
                room.getPlayers().stream()
                        .map(p -> new PlayerDetail(
                                p.getId(),
                                p.getNickname(),
                                p.getRole(),
                                p.isHost(),
                                p.isReady()
                        )).toList()
        );
    }
}