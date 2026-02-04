package com.project.gyeong_do_go.game.dto.response;

import com.project.gyeong_do_go.player.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameResultResponse {
    private Role winnerTeam;
    private String mvpPlayer;
    private String mvpReason;

    @Getter
    @Builder
    public class MvpResult{
        private String nickname;
        private String reason;
    }
}

