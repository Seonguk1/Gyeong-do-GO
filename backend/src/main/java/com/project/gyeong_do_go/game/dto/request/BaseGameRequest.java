package com.project.gyeong_do_go.game.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseGameRequest {
    @NotNull(message = "NULL")
    private Long roomId;

    @NotNull(message = "NULL")
    private Long playerId;
}