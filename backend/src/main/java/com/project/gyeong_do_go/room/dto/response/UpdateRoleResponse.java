package com.project.gyeong_do_go.room.dto.response;

import com.project.gyeong_do_go.room.domain.Role;

public record UpdateRoleResponse(
        Long playerId,
        Role updatedRole
) {}