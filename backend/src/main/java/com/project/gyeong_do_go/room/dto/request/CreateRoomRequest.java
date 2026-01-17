// File: src/main/java/com/project/gyeong_do_go/room/presentation/request/CreateRoomRequest.java
package com.project.gyeong_do_go.room.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRoomRequest {

    @Schema(example = "1", description = "방장 사용자 ID")
    @NotNull
    private Long hostUserId;

    @Schema(example = "테스트방", description = "방 제목(선택)")
    @Size(max = 100)
    private String title;

    @Schema(example = "4", description = "정원(최소 2)")
    @Min(2)
    @Max(100)
    private Integer capacity;
}
