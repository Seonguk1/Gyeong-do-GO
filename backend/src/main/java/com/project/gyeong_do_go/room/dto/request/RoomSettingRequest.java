package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.NotNull;

public record RoomSettingRequest(
        @NotNull(message = "NULL") Double centerLat,
        @NotNull(message = "NULL") Double centerLng,
        @NotNull(message = "NULL") int mapRadius,
        @NotNull(message = "NULL") int prisonRadius,
        @NotNull(message = "NULL") int timeLimit,
        @NotNull(message = "NULL") int runawayLimit
) {}