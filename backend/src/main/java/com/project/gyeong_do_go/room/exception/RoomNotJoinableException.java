// File: src/main/java/com/project/gyeong_do_go/room/exception/RoomNotJoinableException.java
package com.project.gyeong_do_go.room.exception;

import com.project.gyeong_do_go.global.error.BusinessException;
import com.project.gyeong_do_go.global.error.ErrorCode;

public class RoomNotJoinableException extends BusinessException {
    public RoomNotJoinableException() {
        super(ErrorCode.ROOM_NOT_JOINABLE, "Room is not joinable");
    }
}
