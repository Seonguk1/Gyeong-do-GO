package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.game.service.GameFlowService;
import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.dto.request.GameStartRequest;
import com.project.gyeong_do_go.room.dto.request.JoinRoomRequest;
import com.project.gyeong_do_go.room.dto.request.RoomSettingRequest;
import com.project.gyeong_do_go.room.dto.response.*;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final GameFlowService flowService;
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

    @PutMapping("/{roomId}/settings")
    public ApiResponse<RoomSettingResponse> updateRoomSettings(
            @PathVariable Long roomId,
            @RequestBody RoomSettingRequest request
            // @AuthenticationPrincipal 등으로 방장 본인인지 체크 필요
    ) {
        roomService.updateRoomSettings(roomId, request);
        return ApiResponse.success();
    }

    @GetMapping("/{roomId}")
    public ApiResponse<RoomDetailResponse> getRoomDetail(
            @PathVariable Long roomId,
            @RequestParam Long playerId
    ) {
        Room room = roomService.getRoomDetail(roomId);
        return ApiResponse.success(RoomDetailResponse.from(room, playerId));
    }

    @PostMapping("/{roomId}/start")
    public ApiResponse<GameStartResponse> startGame(
            @PathVariable Long roomId,
            @RequestBody @Valid GameStartRequest request
    ) {
        flowService.startGame(request.playerId());
        Room room = roomService.getRoomDetail(roomId);

        return ApiResponse.success(new GameStartResponse(
                room.getRoomStatus().name(),
                room.getStartedAt().toString()
        ));
    }


}