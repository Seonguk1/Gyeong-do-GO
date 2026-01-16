package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.common.exception.ApiResponse;
import com.project.gyeong_do_go.room.dto.RoomCreateRequest;
import com.project.gyeong_do_go.room.dto.RoomCreateResponse;
import com.project.gyeong_do_go.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @Controller: HTML 페이지 반환, @RestController: 데이터(JSON)를 주고받음
@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 선언
@RequestMapping("/api/rooms") // 이 컨트롤러의 모든 API 주소는 /api/rooms로 시작함
@RequiredArgsConstructor // 생성자 주입을 자동으로 생성 (Service 사용을 위함)
public class RoomController {

    private final RoomService roomService; // 비즈니스 로직을 처리할 서비스

    @PostMapping // 클라이언트가 POST 방식으로 요청을 보낼 때 실행됨
    public ResponseEntity<ApiResponse<RoomCreateResponse>> createRoom(@Valid @RequestBody RoomCreateRequest request) {
        request.validate();
        RoomCreateResponse response = roomService.createRoom(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, response));
    }
}