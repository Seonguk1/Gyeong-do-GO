package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.room.dto.request.CreateRoomRequest;
import com.project.gyeong_do_go.room.dto.request.JoinRoomRequest;
import com.project.gyeong_do_go.room.dto.request.StartGameRequest;
import com.project.gyeong_do_go.room.dto.response.CreateRoomResponse;
import com.project.gyeong_do_go.room.dto.response.JoinRoomResponse;
import com.project.gyeong_do_go.room.dto.response.RoomSnapshotDto;
import com.project.gyeong_do_go.room.dto.response.WsEnvelope;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
<<<<<<< HEAD
@RequestMapping("/api/rooms")
=======
>>>>>>> e3f29b75eb3f4b47fe14923e00dce71587760b06
public class RoomController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest req) {
        RoomService.CreateRoomResult result =
                roomService.createRoom(req.getHostUserId(), req.getTitle(), req.getCapacity());
        
        return ResponseEntity.ok(ApiResponse.success(new CreateRoomResponse(result.roomId(), result.code())));
=======
    public CreateRoomResponse create(@RequestBody CreateRoomRequest req) {
        Room room = roomService.createRoom(req.getNickname());

        CreateRoomResponse res = new CreateRoomResponse(
                room.getRoomId(),
                room.getJoinCode(),
                room.getHostPlayerId(),
                true
        );

        broadcastSnapshot(room.getRoomId());
        return res;
>>>>>>> e3f29b75eb3f4b47fe14923e00dce71587760b06
    }

    @PostMapping("/join")
    public JoinRoomResponse join(@RequestBody JoinRoomRequest req) {
        RoomService.JoinResult jr = roomService.joinRoom(req.getJoinCode(), req.getNickname());
        JoinRoomResponse res = new JoinRoomResponse(jr.roomId, jr.playerId);

        broadcastSnapshot(jr.roomId);
        return res;
    }

    @GetMapping("/{roomId}")
    public RoomSnapshotDto get(@PathVariable String roomId) {
        return roomService.getSnapshot(roomId);
    }

    @PostMapping("/{roomId}/start")
    public void start(@PathVariable String roomId, @RequestBody StartGameRequest req) {
        roomService.startGame(roomId, req.getPlayerId());

        RoomSnapshotDto snapshot = roomService.getSnapshot(roomId);
        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId,
                WsEnvelope.of("GAME_STARTED", snapshot)
        );
    }

    private void broadcastSnapshot(String roomId) {
        RoomSnapshotDto snapshot = roomService.getSnapshot(roomId);
        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId,
                WsEnvelope.of("ROOM_SNAPSHOT", snapshot)
        );
    }
}
