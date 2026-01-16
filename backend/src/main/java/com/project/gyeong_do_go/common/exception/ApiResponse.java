package com.project.gyeong_do_go.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder // 컴파일 시점에서 빌더 코드 자동 생성
public class ApiResponse<T> {
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final boolean success; // 성공 여부 (true/false)
    private final T data;          // 실제 전달할 데이터 (성공 시에만 존재)
    private final ErrorResponse error; // 에러 정보 (실패 시에만 존재)

    // 성공 응답 정적 팩토리 메서드 (사용하기 편하게)
    public static <T> ApiResponse<T> success(int status, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .success(true)
                .data(data)
                .error(null)
                .build();
    }

    // 실패 응답 정적 팩토리 메서드
    public static <T> ApiResponse<T> fail(int status, String code, String message, Object details) {
        return ApiResponse.<T>builder()
                .status(status)
                .success(false)
                .data(null)
                .error(new ErrorResponse(code, message, details))
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String code;
        private final String message;
        private final Object details;
    }
}