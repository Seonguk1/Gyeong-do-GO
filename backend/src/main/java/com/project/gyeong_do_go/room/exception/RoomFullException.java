// File: src/main/java/com/project/gyeong_do_go/room/exception/RoomFullException.java
package com.project.gyeong_do_go.room.exception;

import com.project.gyeong_do_go.global.error.BusinessException;
import com.project.gyeong_do_go.global.error.ErrorCode;

public class RoomFullException extends BusinessException {
    public RoomFullException() {
        super(ErrorCode.ROOM_FULL, "Room is full");
    }
}
