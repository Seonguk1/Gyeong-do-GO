package com.project.gyeong_do_go.game.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseGameRequest {
    private Long roomId;
    private Long playerId;
}