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
        @NotNull(message = "NULL")
        RoomSettingsRequest roomSettings
) {
    public record RoomSettingsRequest(
            @NotNull(message = "NULL")
            RoleAssignMode roleAssignMode,

            @NotNull(message = "NULL")
            @Min(value = 10, message = "TOO_SHORT")
            Integer roleRevealSeconds,

            @NotNull(message = "NULL")
            @Min(value = 10, message = "TOO_SHORT")
            Integer thiefEscapeSeconds,

            @NotNull(message = "NULL")
            @Min(value = 10, message = "TOO_SHORT")
            Integer policeChaseSeconds
    ){
        public RoomSettings toEntity() {
            return new RoomSettings(roleAssignMode, roleRevealSeconds, thiefEscapeSeconds, policeChaseSeconds);
        }
    }
}
