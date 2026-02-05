package com.project.gyeong_do_go.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", "서버 내부 오류입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM_NOT_FOUND", "존재하지 않는 방입니다."),
    ROOM_FULL(HttpStatus.CONFLICT, "ROOM_FULL", "방이 꽉 찼습니다."),
    GAME_ALREADY_STARTED(HttpStatus.CONFLICT, "GAME_STARTED", "이미 게임이 시작되었습니다."),

    // Player
    PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAYER_NOT_FOUND", "플레이어를 찾을 수 없습니다."),
    NOT_HOST(HttpStatus.FORBIDDEN, "NOT_HOST", "방장만 접근 가능합니다."),

    // Game
    DISTANCE_TOO_FAR(HttpStatus.OK, "DISTANCE_TOO_FAR", "거리가 너무 멉니다.");

    private final HttpStatus httpStatus;
    private final String code;    // 응답 JSON의 "code" 필드에 들어갈 값
    private final String message; // (선택) 서버 로그용 메시지
}