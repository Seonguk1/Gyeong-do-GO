package com.project.gyeong_do_go.game.dto.response;

import com.project.gyeong_do_go.player.domain.Role;
import lombok.Builder;

@Builder
public class GameResultResponse {
    private Role winnerTeam;
}
