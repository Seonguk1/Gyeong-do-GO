package com.project.gyeong_do_go.game.domain;

public enum GameMessageType {
    JOIN_ROOM,          // 소켓 입장
    SEND_LOCATION,      // 위치 전송
    UPDATE_ROOM,        // 방 정보 갱신
    GAME_STATUS,        // 게임 상태 변경
    UPDATE_POSITIONS,   // 위치 수신
    PLAYER_STATE_CHANGE,// 플레이어 상태
    GAME_OVER           // 게임 종료
}