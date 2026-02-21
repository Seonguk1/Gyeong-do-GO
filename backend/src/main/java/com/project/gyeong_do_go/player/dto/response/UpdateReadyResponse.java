package com.project.gyeong_do_go.player.dto.response;

public record UpdateReadyResponse(
        Long playerId,
        boolean isReady
) {}