package com.project.gyeong_do_go.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerDto {
    private final String playerId;
    private final String nickname;
    private final boolean ready;
}
