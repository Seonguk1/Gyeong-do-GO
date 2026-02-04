package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.CatchResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    private final GameFlowService gameFlowService;

    @Transactional
    public void joinGame(Long playerId) {
        Player player = gameReader.getPlayer(playerId);
        Long roomId = player.getRoom().getId();
        gameBroadcaster.broadcastRoomInfo(roomId);
    }

    @Transactional
    public void leaveGame(Long playerId) {
        Player player = gameReader.getPlayer(playerId);

        Room room = player.getRoom();
        Long roomId = room.getId();
        GameStatus roomStatus = room.getRoomStatus();

        log.info("플레이어 퇴장 요청: nick={}, roomStatus={}", player.getNickname(), roomStatus);

        if (roomStatus == GameStatus.WAITING) {
            if (player.isHost()) {
                gameRepository.updateRoomStatus(roomId, "FINISHED");
                gameBroadcaster.sendToRoom(roomId,GameMessageType.GAME_OVER,"HOST_LEFT");
                roomRepository.delete(room); // 실제 방 삭제는 나중에 스케줄러가 해도 됨
                return;
            }
            playerRepository.delete(player);
            gameBroadcaster.broadcastRoomInfo(roomId);
        }
        else if (roomStatus == GameStatus.ROLE_CHECK || roomStatus == GameStatus.RUNAWAY || roomStatus == GameStatus.PLAYING) {
            if (player.getStatus() == PlayerStatus.OUT) return;

            player.updateStatus(PlayerStatus.OUT);

            CatchResponse leaveMsg = CatchResponse.builder()
                    .policeNickname("SYSTEM") // 시스템 알림
                    .thiefNickname(player.getNickname()) // 나간 사람
                    .thiefId(player.getId())
                    .build();

            gameBroadcaster.sendToRoom(roomId, GameMessageType.PLAYER_LEFT, leaveMsg);
            gameFlowService.checkGameOverCondition(roomId);
        }
    }

    @Transactional
    public void handleDisconnect(Long playerId) {
        Player player = gameReader.getPlayer(playerId);
        Long roomId = player.getRoom().getId();
        GameStatus status = gameRepository.getRoomStatus(roomId);
        log.info("퇴장 처리 로직 실행: 방 상태={}, 플레이어={}", status, player.getNickname());

        if (status == GameStatus.WAITING || status == GameStatus.ROLE_CHECK) {
            playerRepository.delete(player);
            gameBroadcaster.broadcastRoomInfo(roomId);
        } else if (status == GameStatus.RUNAWAY || status == GameStatus.PLAYING) {
            if (PlayerStatus.OUT.equals(player.getStatus())) return;

            // 탈주로 간주하고 OUT 처리
            gameRepository.updatePlayerStatus(playerId, PlayerStatus.OUT);

            CatchResponse disconnectData = CatchResponse.builder()
                    .policeNickname("SYSTEM(탈주)")
                    .thiefNickname(player.getNickname())
                    .thiefId(player.getId())
                    .build();
            gameBroadcaster.sendToRoom(roomId, GameMessageType.PLAYER_CAUGHT, disconnectData);

            gameFlowService.checkGameOverCondition(roomId);
        }
    }

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시 실행
    @Transactional
    public void cleanupOldRooms() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        // 어제 이전에 만들어진 방들 모두 삭제 (Cascade 설정 때문에 Player도 같이 삭제됨)
        roomRepository.deleteByCreatedAtBefore(yesterday);
        log.info("오래된 방 데이터 삭제 완료");
    }
}
