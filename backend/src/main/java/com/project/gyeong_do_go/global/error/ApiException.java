package com.project.gyeong_do_go.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode code;
    private final String field;
    private final ErrorReason reason;

    public ApiException(HttpStatus status, ErrorCode code){
        this.status = status;
        this.code = code;
        this.field = null;
        this.reason = null;
    }
}
