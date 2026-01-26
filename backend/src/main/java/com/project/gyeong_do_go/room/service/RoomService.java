package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.error.ApiException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.room.dto.response.RoomStateResponse;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.entity.RoomSettings;
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
    public RoomStateResponse createRoom(String nickname, RoomSettings roomSettings) {
        Room room = roomRepository.save(new Room("123456", roomSettings));

        Player host = playerRepository.save(new Player(nickname, room));
        room.setHost(host);

        RoomStateResponse state = buildRoomState(room);
        broadcastRoomState(state);
        return state;
    }

    @Transactional
    public RoomStateResponse joinRoom(String code, String nickname) {
        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

        Player player = playerRepository.save(new Player(nickname, room));

        RoomStateResponse state = buildRoomState(room);
        broadcastRoomState(state);
        return state;
    }

    private RoomStateResponse buildRoomState(Room room) {
        List<Player> players = playerRepository.findAllByRoom_Id(room.getId());

        List<RoomStateResponse.PlayerInfo> infos = new ArrayList<>();
        for (Player p : players) {
            infos.add(new RoomStateResponse.PlayerInfo(p.getNickname(), p.getTeam()));
            room.addPlayer(p);
        }
        return new RoomStateResponse(room.getCode(), room.getSettings(), infos.size(), infos);
    }

    private void broadcastRoomState(RoomStateResponse roomStateResponse){
        messagingTemplate.convertAndSend("/topic/rooms/" + roomStateResponse.code(), roomStateResponse);
    }
}
