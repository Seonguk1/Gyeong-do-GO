package com.project.gyeong_do_go.game.component;

import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameReader {

    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;

    // 1. ID로 플레이어 찾기
    public Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));
    }

    // 2. ID로 방 찾기
    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    // 3. 방 안의 플레이어 리스트 찾기 (방 ID 기준)
    public List<Player> getPlayersInRoom(Long roomId) {
        // 방이 존재하는지 먼저 체크하는 것이 안전함
        if (!roomRepository.existsById(roomId)) {
            throw new CustomException(ErrorCode.ROOM_NOT_FOUND);
        }
        return playerRepository.findByRoomId(roomId);
    }
}