package com.project.gyeong_do_go.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoomSnapshotDto {
    private final String roomId;
    private final String joinCode;
    private final String hostPlayerId;
    private final String status; // "LOBBY" | "IN_GAME"
    private final List<PlayerDto> players;
}
