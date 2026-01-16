package com.project.gyeong_do_go.common.exception;

import lombok.Getter;

// 서비스나 DTO에서 에러를 던질 수 있게 함
// throw new BusinessException(ErrorCode.MISSING_SEEKER_COUNT);
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}