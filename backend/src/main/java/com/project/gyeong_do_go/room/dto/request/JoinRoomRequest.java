package com.project.gyeong_do_go.room.dto.request;

import jakarta.validation.constraints.*;

public record JoinRoomRequest (
        @NotBlank(message = "BLANK")
        String code,

        @NotBlank(message = "BLANK")
        @Size(min=2, max=20, message="INVALID_LENGTH")
        String nickname
) { }
