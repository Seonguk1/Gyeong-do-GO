package com.project.gyeong_do_go.room.controller;

import com.project.gyeong_do_go.room.dto.request.ReadyRequest;
import com.project.gyeong_do_go.room.dto.response.RoomSnapshotDto;
import com.project.gyeong_do_go.room.dto.response.WsEnvelope;
import com.project.gyeong_do_go.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomWsController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}/ready")
    public void ready(@DestinationVariable String roomId, @Payload ReadyRequest req) {
        roomService.setReady(roomId, req.getPlayerId(), req.isReady());

        RoomSnapshotDto snapshot = roomService.getSnapshot(roomId);
        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId,
                WsEnvelope.of("ROOM_SNAPSHOT", snapshot)
        );
    }
}
