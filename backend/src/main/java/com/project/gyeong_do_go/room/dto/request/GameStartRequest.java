package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.NotNull;

public record GameStartRequest(
        @NotNull(message = "NULL") Long playerId
) {}