// File: src/main/java/com/project/gyeong_do_go/global/response/ApiResponse.java
package com.project.gyeong_do_go.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "공통 API 응답 포맷")
public class ApiResponse<T> {

    @Schema(example = "SUCCESS")
    private ResultType result;

    @Schema(description = "성공 시 응답 데이터, 없으면 null")
    private T data;

    @Schema(description = "실패 시 에러 정보, 성공이면 null")
    private ApiError error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(ResultType.ERROR, null, ApiError.of(code, message));
    }

    public static <T> ApiResponse<T> error(String code, String message, java.util.List<FieldErrorData> data) {
        return new ApiResponse<>(ResultType.ERROR, null, ApiError.of(code, message, data));
    }
}
