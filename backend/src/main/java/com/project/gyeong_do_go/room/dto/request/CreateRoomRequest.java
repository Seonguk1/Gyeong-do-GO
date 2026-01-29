package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRoomRequest(
        @NotBlank(message = "BLANK") String nickname,
        @NotNull(message = "NULL") Double latitude,
        @NotNull(message = "NULL") Double longitude,
        @NotNull(message = "NULL") int mapRadius,
        @NotNull(message = "NULL") int prisonRadius,
        @NotNull(message = "NULL") int timeLimit,
        @NotNull(message = "NULL") int runawayLimit
) {}