// File: src/main/java/com/project/gyeong_do_go/room/dto/response/JoinRoomResponse.java
package com.project.gyeong_do_go.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinRoomResponse {
    private Long roomId;
    private Long memberId;
}
