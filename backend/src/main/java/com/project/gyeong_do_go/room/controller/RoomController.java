// File: src/main/java/com/project/gyeong_do_go/room/dto/RoomController.java
package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.global.response.ApiResponse;
import com.project.gyeong_do_go.room.service.RoomService;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.dto.request.JoinRoomRequest;
import com.project.gyeong_do_go.room.dto.response.CreateRoomResponse;
import com.project.gyeong_do_go.room.dto.response.JoinRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room", description = "방 생성/참가 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "방 생성", description = "호스트가 방을 생성하고 초대코드를 발급합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest req) {
        RoomService.CreateRoomResult result =
                roomService.createRoom(req.getHostUserId(), req.getTitle(), req.getCapacity());

        return ResponseEntity.ok(ApiResponse.success(new CreateRoomResponse(result.roomId(), result.code())));
    }

    @Operation(summary = "방 참가", description = "초대코드로 방에 참가합니다.")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinRoomResponse>> join(@Valid @RequestBody JoinRoomRequest req) {
        RoomService.JoinRoomResult result =
                roomService.joinRoom(req.getUserId(), req.getCode());

        return ResponseEntity.ok(ApiResponse.success(new JoinRoomResponse(result.roomId(), result.memberId())));
    }
}
