package com.project.gyeong_do_go.player.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateReadyRequest(
        @NotNull(message = "NULL") Long playerId,
        @NotNull(message = "NULL") boolean isReady
) {}