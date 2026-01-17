// File: src/main/java/com/project/gyeong_do_go/global/error/BusinessException.java
package com.project.gyeong_do_go.global.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
