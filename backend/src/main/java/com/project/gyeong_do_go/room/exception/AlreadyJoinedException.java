// File: src/main/java/com/project/gyeong_do_go/room/exception/AlreadyJoinedException.java
package com.project.gyeong_do_go.room.exception;

import com.project.gyeong_do_go.global.error.BusinessException;
import com.project.gyeong_do_go.global.error.ErrorCode;

public class AlreadyJoinedException extends BusinessException {
  public AlreadyJoinedException() {
    super(ErrorCode.ALREADY_JOINED, "Already joined");
  }
}
