package com.project.gyeong_do_go.player.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameValidator;
import com.project.gyeong_do_go.global.entity.RoomAndPlayer;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final GameBroadcaster gameBroadcaster;
    private final GameValidator gameValidator;

    @Transactional
    public void updatePlayerRole(Long roomId, Long playerId, Role newRole) {
        RoomAndPlayer roomAndPlayer = gameValidator.validateAndGet(roomId, playerId);
        Room room = roomAndPlayer.room(); Player player = roomAndPlayer.player();

        if (room.getRoomStatus() != GameStatus.WAITING) throw new CustomException(ErrorCode.GAME_ALREADY_STARTED);

        if (player.getRole() == newRole) return;

        player.setRole(newRole);
        player.setReady(player.isHost());
        gameBroadcaster.broadcastRoomInfo(roomId);
    }

    @Transactional
    public void updatePlayerReady(Long roomId, Long playerId, boolean isReady) {
        RoomAndPlayer roomAndPlayer = gameValidator.validateAndGet(roomId, playerId);
        Room room = roomAndPlayer.room(); Player player = roomAndPlayer.player();

        if (player.isHost()) throw new CustomException(ErrorCode.HOST_CANNOT_CHANGE_READY);

        if (room.getRoomStatus() != GameStatus.WAITING) throw new CustomException(ErrorCode.GAME_ALREADY_STARTED);

        player.setReady(!isReady);
        gameBroadcaster.broadcastRoomInfo(roomId);
    }
}
