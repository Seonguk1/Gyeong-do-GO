package com.project.gyeong_do_go.game.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameStartResponse {
    private String roomStatus;
    private String finishTime;
}