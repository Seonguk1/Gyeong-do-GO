package com.project.gyeong_do_go.room.dto.response;

import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.room.entity.Room;

import java.util.List;

public record RoomDetailResponse(
        Long roomId,
        String roomCode,
        String roomStatus,

        Double centerLat,
        Double centerLng, // 혹은 centerLon (변수명 통일 필요)
        Integer mapRadius,
        Integer prisonRadius,
        Integer timeLimit,
        Integer runawayLimit,

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
                room.getRoomStatus().name(),

                room.getCenterLat(),
                room.getCenterLng(),
                room.getMapRadius(),
                room.getPrisonRadius(),
                room.getTimeLimit(),
                room.getRunawayLimit(),

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