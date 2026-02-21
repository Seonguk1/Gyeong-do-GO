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

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증되지 않은 사용자입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM_NOT_FOUND", "존재하지 않는 방입니다."),
    ROOM_FULL(HttpStatus.CONFLICT, "ROOM_FULL", "방이 꽉 찼습니다."),
    ROOM_NOT_JOINABLE(HttpStatus.CONFLICT, "ROOM_NOT_JOINABLE", "현재 방에 참가할 수 없는 상태입니다."), // 게임 중 등

    // Player
    PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAYER_NOT_FOUND", "플레이어를 찾을 수 없습니다."),
    NOT_HOST(HttpStatus.FORBIDDEN, "NOT_HOST", "방장만 접근 가능합니다."),
    PLAYER_NOT_IN_ROOM(HttpStatus.FORBIDDEN, "PLAYER_NOT_IN_ROOM", "플레이어가 해당 방에 존재하지 않습니다."), // 403 적용
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "NICKNAME_DUPLICATED", "이미 사용 중인 닉네임입니다."),
    HOST_CANNOT_CHANGE_READY(HttpStatus.CONFLICT, "HOST_CANNOT_CHANGE_READY", "방장은 준비 상태를 변경할 수 없습니다."),

    // Game Start
    NOT_ALL_READY(HttpStatus.CONFLICT, "NOT_ALL_READY", "모든 플레이어가 준비 상태가 되어야 합니다."),
    NOT_ENOUGH_PLAYERS(HttpStatus.CONFLICT, "NOT_ENOUGH_PLAYERS", "게임 시작을 위한 최소 인원이 부족합니다."),

    // Game / Action
    GAME_NOT_STARTED(HttpStatus.CONFLICT, "GAME_NOT_STARTED", "게임이 아직 시작되지 않았습니다."),
    GAME_ALREADY_STARTED(HttpStatus.CONFLICT, "GAME_ALREADY_STARTED", "이미 게임이 시작되었습니다."),
    DISTANCE_TOO_FAR(HttpStatus.BAD_REQUEST, "DISTANCE_TOO_FAR", "거리가 너무 멉니다."); // 200 OK 보다는 400이 더 적절할 수 있음 (클라이언트 실수이므로)

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}