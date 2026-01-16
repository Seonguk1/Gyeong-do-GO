package com.project.gyeong_do_go.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder // 컴파일 시점에서 빌더 코드 자동 생성
public class ApiResponse<T> {
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final boolean success; // 성공 여부 (true/false)
    private final T data;          // 실제 전달할 데이터 (성공 시에만 존재)
    private final ErrorDetail error; // 에러 정보 (실패 시에만 존재)

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
    public static <T> ApiResponse<T> fail(int status, String code, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .success(false)
                .data(null)
                .error(new ErrorDetail(code, message))
                .build();
    }

    @Getter
    @AllArgsConstructor
    static class ErrorDetail {
        private String code;
        private String message;
    }
}