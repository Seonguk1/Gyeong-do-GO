package com.project.gyeong_do_go.room.dto.response;

public record JoinRoomResponse(
        Long roomId,
        String roomCode,
        Long playerId,
        String roomStatus
) {}