package com.project.gyeong_do_go.room.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadyRequest {
    private String playerId;
    private boolean ready;
}
