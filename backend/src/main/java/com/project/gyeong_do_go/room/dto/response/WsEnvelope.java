package com.project.gyeong_do_go.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WsEnvelope<T> {
    private final String type;
    private final T payload;

    public static <T> WsEnvelope<T> of(String type, T payload) {
        return new WsEnvelope<>(type, payload);
    }
}
