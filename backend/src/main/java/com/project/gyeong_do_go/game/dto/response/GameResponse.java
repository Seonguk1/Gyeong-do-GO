package com.project.gyeong_do_go.game.dto.response;

import com.project.gyeong_do_go.game.domain.GameMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse<T> {
    private GameMessageType type;
    private T data;
}
