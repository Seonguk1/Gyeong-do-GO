package com.project.gyeong_do_go.game.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatchResponse {
    private Long policeId;
    private String policeNickname;
    private Long thiefId;
    private String thiefNickname;
}
