package com.project.gyeong_do_go.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // --- 공통 에러 ---
    INVALID_INPUT_VALUE(400, "COMMON_001", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "COMMON_002", "허용되지 않은 HTTP 메서드입니다."),

    // --- 방 관련 에러 ---
    INVALID_TITLE(400, "ROOM_001", "방 제목은 필수이며 공백일 수 없습니다."),
    INVALID_PLAYER_COUNT(400, "ROOM_002", "인원수는 2명에서 20명 사이여야 합니다."),
    MISSING_SEEKER_COUNT(400, "ROOM_003", "클래식/좀비 모드에서는 술래 수가 필수입니다."),
    ROOM_NOT_FOUND(404, "ROOM_004", "해당 방을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}