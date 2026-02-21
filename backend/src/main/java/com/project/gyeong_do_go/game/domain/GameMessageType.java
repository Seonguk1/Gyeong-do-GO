package com.project.gyeong_do_go.game.domain;

public enum GameMessageType {
    // SendToRoom
    UPDATE_ROOM,
    ROOM_STATUS_CHANGE,
    UPDATE_LOCATION,
    UPDATE_LOCATIONS,
    PLAYER_STATE_CHANGE,
    PLAYER_CAUGHT,
    PLAYER_LEFT,
    PLAYER_RESCUED,
    GAME_OVER,

    // SendToPlayer
    PRISONER_NUMBER
}