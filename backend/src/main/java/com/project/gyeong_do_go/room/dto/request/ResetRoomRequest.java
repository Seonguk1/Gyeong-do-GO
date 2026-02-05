package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.NotNull;

public record ResetRoomRequest(
        @NotNull(message = "NULL") Long playerId
) {}