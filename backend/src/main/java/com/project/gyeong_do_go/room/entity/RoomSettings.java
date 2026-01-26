package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.RoleAssignMode;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest.RoomSettingsRequest;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RoomSettings {

    @Enumerated(EnumType.STRING)
    private RoleAssignMode roleAssignMode;

    private int roleRevealSeconds;

    private int thiefEscapeSeconds;

    private int policeChaseSeconds;
}
