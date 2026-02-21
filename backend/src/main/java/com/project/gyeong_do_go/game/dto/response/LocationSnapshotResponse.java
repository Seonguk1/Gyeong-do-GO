package com.project.gyeong_do_go.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationSnapshotResponse {
    private List<LocationResponse> locations;
    private long timestamp;
}
