package com.project.gyeong_do_go.room.dto.request;

import com.project.gyeong_do_go.room.domain.RoleAssignMode;
import com.project.gyeong_do_go.room.entity.RoomSettings;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

public record CreateRoomRequest (
        @NotBlank
        @Size(min = 2, max = 20)
        String nickname,

        @Valid
        @NotNull
        RoomSettingsRequest roomSettings
) {
    public record RoomSettingsRequest(
            @NotNull
            RoleAssignMode roleAssignMode,

            @Min(10)
            int roleRevealSeconds,

            @Min(10)
            int thiefEscapeSeconds,

            @Min(10)
            int policeChaseSeconds
    ){
        public RoomSettings toEntity() {
            return new RoomSettings(roleAssignMode, roleRevealSeconds, thiefEscapeSeconds, policeChaseSeconds);
        }
    }
}
