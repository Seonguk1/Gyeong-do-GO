package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.dto.request.*;
import com.project.gyeong_do_go.room.dto.response.*;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ApiResponse<CreateRoomResponse> createRoom(@RequestBody @Valid CreateRoomRequest request) {
        Room room = roomService.createRoom(request);
        Player host = room.getPlayers().stream()
                .filter(Player::isHost)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));

        return ApiResponse.success(new CreateRoomResponse(
                room.getId(),
                room.getRoomCode(),
                host.getId(),
                room.getRoomStatus().name()
        ));
    }

    @PostMapping("/join")
    public ApiResponse<JoinRoomResponse> joinRoom(@RequestBody @Valid JoinRoomRequest request) {
        Player player = roomService.joinRoom(request.roomCode(), request.nickname());
        return ApiResponse.success(new JoinRoomResponse(
                player.getRoom().getId(),
                player.getRoom().getRoomCode(),
                player.getId(),
                player.getRoom().getRoomStatus().name()
        ));
    }

    @GetMapping("/{roomId}")
    public ApiResponse<RoomDetailResponse> getRoomDetail(
            @PathVariable Long roomId,
            @RequestParam Long playerId
    ) {
        Room room = roomService.getRoomDetail(roomId);
        return ApiResponse.success(RoomDetailResponse.from(room, playerId));
    }

    @PatchMapping("/{roomId}/start")
    public ApiResponse<GameStartResponse> startGame(
            @PathVariable Long roomId,
            @RequestBody @Valid GameStartRequest request
    ) {
        roomService.startGame(roomId, request.playerId());
        Room room = roomService.getRoomDetail(roomId);

        return ApiResponse.success(new GameStartResponse(
                room.getRoomStatus().name(),
                room.getStartedAt().toString()
        ));
    }

    @PatchMapping("/{roomId}/players/role")
    public ApiResponse<UpdateRoleResponse> updateRole(
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateRoleRequest request
    ) {
        roomService.updatePlayerRole(request.playerId(), request.role());
        return ApiResponse.success(new UpdateRoleResponse(request.playerId(), request.role()));
    }

    @PatchMapping("/{roomId}/players/ready")
    public ApiResponse<UpdateReadyResponse> updateReady(
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateReadyRequest request
    ) {
        roomService.updatePlayerReady(request.playerId(), request.isReady());
        return ApiResponse.success(new UpdateReadyResponse(request.playerId(), request.isReady()));
    }
}