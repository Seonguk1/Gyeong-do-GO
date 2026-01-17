// File: src/main/java/com/project/gyeong_do_go/room/dto/response/CreateRoomResponse.java
package com.project.gyeong_do_go.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRoomResponse {
    private Long roomId;
    private String code;
}
