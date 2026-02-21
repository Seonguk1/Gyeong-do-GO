package com.project.gyeong_do_go.player.dto.response;

import com.project.gyeong_do_go.player.domain.Role;

public record UpdateRoleResponse(
        Long playerId,
        Role updatedRole
) {}