// File: src/main/java/com/project/gyeong_do_go/global/response/ApiError.java
package com.project.gyeong_do_go.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "에러 정보")
public class ApiError {

    @Schema(example = "ROOM_FULL")
    private String code;

    @Schema(example = "Room is full")
    private String message;

    @Schema(description = "검증 에러 상세(없으면 null)")
    private List<FieldErrorData> data;

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null);
    }

    public static ApiError of(String code, String message, List<FieldErrorData> data) {
        return new ApiError(code, message, data);
    }
}
