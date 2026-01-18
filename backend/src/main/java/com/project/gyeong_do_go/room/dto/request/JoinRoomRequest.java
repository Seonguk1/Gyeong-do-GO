// File: src/main/java/com/project/gyeong_do_go/room/dto/request/JoinRoomRequest.java
package com.project.gyeong_do_go.room.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRoomRequest {

    @Schema(example = "2", description = "참가 사용자 ID")
    @NotNull
    private Long userId;

    @Schema(example = "A3K9Q2", description = "초대코드(대문자/숫자)")
    @NotBlank
    @Size(max = 8)
    private String code;
}
