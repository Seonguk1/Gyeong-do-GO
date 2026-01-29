package com.project.gyeong_do_go.room.dto.request;

import com.project.gyeong_do_go.room.domain.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull(message = "NULL") Long playerId,
        @NotNull(message = "NULL") Role role
) {}