package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.ApiException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.dto.response.RoomStateResponse;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.PlayerRepository;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public RoomStateResponse createRoom(String nickname) {
        Room room = new Room("123456"); // TODO: 랜덤 생성으로 교체
        roomRepository.save(room);

        addPlayer(room, nickname);

        RoomStateResponse state = buildRoomState(room);
        broadcastRoomState(state);
        return state;
    }

    @Transactional
    public RoomStateResponse joinRoom(String code, String nickname) {
        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

        addPlayer(room, nickname);

        RoomStateResponse state = buildRoomState(room);
        broadcastRoomState(state);
        return state;
    }

    private void addPlayer(Room room, String nickname) {
        Player player = new Player(nickname, room);
        playerRepository.save(player);
    }

    private RoomStateResponse buildRoomState(Room room) {
        List<Player> players = playerRepository.findAllByRoom_Id(room.getId());

        List<RoomStateResponse.PlayerInfo> infos = new ArrayList<>();
        for (Player p : players) {
            infos.add(new RoomStateResponse.PlayerInfo(p.getNickname(), p.getTeam()));
        }

        return new RoomStateResponse(room.getCode(), infos.size(), infos);
    }

    private void broadcastRoomState(RoomStateResponse roomStateResponse){
        messagingTemplate.convertAndSend("/topic/rooms/" + roomStateResponse.code(), roomStateResponse);
    }
}
