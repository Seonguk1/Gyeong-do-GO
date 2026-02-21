package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.component.GameValidator;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.LeaveResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.global.entity.RoomAndPlayer;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameSessionService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;
    private final GameBroadcaster gameBroadcaster;
    private final GameReader gameReader;
    private final GameValidator gameValidator;
    private final GameFlowService gameFlowService;

    @Transactional
    public void joinGame(Long roomId, Long playerId) {
        gameBroadcaster.broadcastRoomInfo(roomId);
    }

    @Transactional
    public void leaveGame(Long roomId, Long playerId) {
        RoomAndPlayer rp = gameValidator.validateAndGet(roomId, playerId);
        Room room = rp.room(); Player player = rp.player();

        GameStatus roomStatus = room.getRoomStatus();

        log.info("플레이어 퇴장 요청: nick={}, roomStatus={}", player.getNickname(), roomStatus);

        if (roomStatus == GameStatus.WAITING || roomStatus == GameStatus.FINISHED) {
            if (player.isHost()) {
                gameRepository.updateRoomStatus(roomId, "FINISHED");
                gameBroadcaster.sendToRoom(roomId,GameMessageType.GAME_OVER,"HOST_LEFT");
                roomRepository.delete(room); // 실제 방 삭제는 나중에 스케줄러가 해도 됨
                return;
            }
            room.getPlayers().remove(player);
            playerRepository.delete(player);
            gameBroadcaster.broadcastRoomInfo(roomId);
        }
        else {
            if (player.getStatus() == PlayerStatus.OUT) return;

            player.updateStatus(PlayerStatus.OUT);

            LeaveResponse leaveMsg = LeaveResponse.builder()
                    .playerId(player.getId())
                    .playerNickname(player.getNickname())
                    .build();

            gameBroadcaster.sendToRoom(roomId, GameMessageType.PLAYER_LEFT, leaveMsg);
            gameFlowService.checkGameOverCondition(roomId);
        }
    }

    @Transactional
    public void handleDisconnect(Long roomId, Long playerId) {
        RoomAndPlayer rp = gameValidator.validateAndGet(roomId, playerId);
        Room room = rp.room(); Player player = rp.player();
        GameStatus status = room.getRoomStatus();

        if (!playerRepository.existsById(playerId)) return;

        log.info("퇴장 처리 로직 실행: 방 상태={}, 플레이어={}", status, player.getNickname());

        leaveGame(roomId, playerId);
    }
}
