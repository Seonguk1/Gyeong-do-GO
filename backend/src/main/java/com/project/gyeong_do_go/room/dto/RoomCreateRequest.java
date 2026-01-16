package com.project.gyeong_do_go.room.dto;

import com.project.gyeong_do_go.common.exception.BusinessException;
import com.project.gyeong_do_go.common.exception.ErrorCode;
import com.project.gyeong_do_go.room.domain.GameMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomCreateRequest {

    @NotBlank(message = "방 제목은 필수입니다.")
    private String title;

    @Min(value = 2, message = "최소 인원은 2명입니다.")
    @Max(value = 20, message = "최대 인원은 20명입니다.")
    private int maxPlayers;

    @Positive(message = "제한 시간은 양수여야 합니다.")
    private int timeLimit;

    @NotNull(message = "게임 모드는 필수입니다.")
    private GameMode mode;

    @Valid // @Valid를 붙여야 내부의 검증 조건도 같이 검사
    @NotNull(message = "세부 설정은 필수입니다.")
    private GameDetails details;

    @Getter
    @NoArgsConstructor
    public static class GameDetails {
        @Min(value = 1, message = "술래는 최소 1명이어야 합니다.")
        private Integer seekerCount;
        private String teamDivide;
    }

    public void validate() {
        if (this.mode == GameMode.CLASSIC && (details == null || details.getSeekerCount() == null)) {
            throw new BusinessException(ErrorCode.MISSING_SEEKER_COUNT);
        }

        if (this.mode == GameMode.TEAM_BATTLE && (details == null || details.getTeamDivide() == null)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
