package com.project.gyeong_do_go.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.gyeong_do_go.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    // 생성자 (Private)
    private ApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // 성공 응답 (Static Factory)
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null);
    }
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 에러 응답 (Static Factory)
    public static ApiResponse<Void> fail(ErrorCode errorCode, Object details) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCode.getCode(), details));
    }

    // --- Inner Class for Error Structure ---
    @Getter
    public static class ErrorResponse {
        private final String code;

        // details는 String일 수도 있고, List<ValidationError>일 수도 있어서 Object로 선언
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final Object details;

        public ErrorResponse(String code, Object details) {
            this.code = code;
            this.details = details;
        }
    }
}