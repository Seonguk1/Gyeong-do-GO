package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.NotBlank;

public record JoinRoomRequest(
        @NotBlank(message = "BLANK") String roomCode,
        @NotBlank(message = "BLANK") String nickname
) {}