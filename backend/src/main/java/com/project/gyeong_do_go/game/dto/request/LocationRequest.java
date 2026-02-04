package com.project.gyeong_do_go.game.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    @NotNull(message = "NULL")
    private double latitude;
    @NotNull(message = "NULL")
    private double longitude;
}