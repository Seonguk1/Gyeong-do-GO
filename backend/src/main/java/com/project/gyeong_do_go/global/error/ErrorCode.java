// File: src/main/java/com/project/gyeong_do_go/global/error/ErrorCode.java
package com.project.gyeong_do_go.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM_NOT_FOUND"),
    ROOM_NOT_JOINABLE(HttpStatus.CONFLICT, "ROOM_NOT_JOINABLE"),
    ROOM_FULL(HttpStatus.CONFLICT, "ROOM_FULL"),
    ALREADY_JOINED(HttpStatus.CONFLICT, "ALREADY_JOINED"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR");

    private final HttpStatus httpStatus;
    private final String code;
}
