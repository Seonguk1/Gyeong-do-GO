package com.project.gyeong_do_go.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private Long playerId;
    private double latitude;
    private double longitude;
}