// File: src/main/java/com/project/gyeong_do_go/room/exception/RoomNotFoundException.java
package com.project.gyeong_do_go.room.exception;

import com.project.gyeong_do_go.global.error.BusinessException;
import com.project.gyeong_do_go.global.error.ErrorCode;

public class RoomNotFoundException extends BusinessException {
    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND, "Room not found");
    }
}
