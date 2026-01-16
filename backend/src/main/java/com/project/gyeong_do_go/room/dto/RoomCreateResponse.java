package com.project.gyeong_do_go.room.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RoomCreateResponse {
    private final String roomCode;
    private final String hostNickname;
    private final LocalDateTime createdAt;
}