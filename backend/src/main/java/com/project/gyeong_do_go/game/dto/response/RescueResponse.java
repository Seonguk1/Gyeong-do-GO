package com.project.gyeong_do_go.game.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RescueResponse {
    private Long rescuerId;
    private String rescuerNickname;
    private int rescuedCount;
    private List<String> rescuedNicknames;
}
