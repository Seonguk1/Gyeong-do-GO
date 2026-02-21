package com.project.gyeong_do_go.game.dto.response;

import com.project.gyeong_do_go.room.domain.GameStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameStartResponse {
    private GameStatus roomStatus;
    private String finishTime;
}