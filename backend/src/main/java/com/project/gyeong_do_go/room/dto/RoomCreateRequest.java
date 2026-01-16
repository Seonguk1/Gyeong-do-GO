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
    @NotBlank(message = "{NotBlank}")
    @Size(min = 2, message = "{Size.min}")
    @Size(max = 20, message = "{Size.max}")
    private String hostNickname;

    @NotBlank(message = "{NotBlank}")
    @Size(min = 2, message = "{Size.min}")
    @Size(max = 20, message = "{Size.max}")
    private String title;

    @Min(value = 10, message = "{Min}")
    @Max(value = 50, message = "{Max}")
    private int capacity;

    @Positive(message = "{Positive}")
    @Max(value = 60, message = "{Max}")
    private int timeLimit;

    @NotNull(message = "{NotNull}")
    private GameMode mode;

    @Valid // @Valid를 붙여야 내부의 검증 조건도 같이 검사
    @NotNull(message = "{NotNull}")
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
