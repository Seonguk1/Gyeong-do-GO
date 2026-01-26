package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.dto.request.JoinRoomRequest;
import com.project.gyeong_do_go.room.dto.response.RoomStateResponse;
import com.project.gyeong_do_go.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomStateResponse>> create(@Valid @RequestBody CreateRoomRequest request) {
        RoomStateResponse response = roomService.createRoom(request.nickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<RoomStateResponse>> join(@Valid @RequestBody JoinRoomRequest request){
        RoomStateResponse response =  roomService.joinRoom(request.code(), request.nickname());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }
}
