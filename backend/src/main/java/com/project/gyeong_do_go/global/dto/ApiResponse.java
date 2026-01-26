package com.project.gyeong_do_go.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.global.error.ErrorReason;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error
) {
    public record ApiError (ErrorCode code, List<ErrorDetail> details) {}
    public record ErrorDetail(String field, ErrorReason reason) {}

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode code) {
        return new ApiResponse<>(false, null, new ApiError(code, null));
    }

    public static <T> ApiResponse<T> fail(ErrorCode code, List<ErrorDetail> details) {
        return new ApiResponse<>(false, null, new ApiError(code, details));
    }
}