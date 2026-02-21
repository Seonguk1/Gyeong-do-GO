package com.project.gyeong_do_go.player.controller;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.player.dto.request.UpdateReadyRequest;
import com.project.gyeong_do_go.player.dto.request.UpdateRoleRequest;
import com.project.gyeong_do_go.player.dto.response.UpdateReadyResponse;
import com.project.gyeong_do_go.player.dto.response.UpdateRoleResponse;
import com.project.gyeong_do_go.player.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/players")
public class RoomPlayerController {
    private final PlayerService playerService;

    @PatchMapping("/role")
    public ApiResponse<UpdateRoleResponse> updateRole(
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateRoleRequest request
    ) {
        playerService.updatePlayerRole(roomId, request.playerId(), request.role());

        return ApiResponse.success(
                new UpdateRoleResponse(request.playerId(), request.role())
        );
    }

    @PatchMapping("/ready")
    public ApiResponse<UpdateReadyResponse> updateReady(
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateReadyRequest request
    ) {
        playerService.updatePlayerReady(roomId, request.playerId(), request.isReady());

        return ApiResponse.success(
                new UpdateReadyResponse(request.playerId(), request.isReady())
        );
    }
}
