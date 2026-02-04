package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResultResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameFlowService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;
    private final GameBroadcaster gameBroadcaster;
    private final GameReader  gameReader;
    private final TaskScheduler taskScheduler;

    // 자기 자신을 주입받음 (순환 참조 방지 위해 @Lazy 사용)
    @Lazy
    @Autowired
    private GameFlowService self;

    @Transactional
    public void startGame(Long playerId) {
        Player player = gameReader.getPlayer(playerId);
        if (!player.isHost()) {
            throw new IllegalStateException("방장만 게임을 시작할 수 있습니다.");
        }
        Room room = player.getRoom();
        Long roomId = room.getId();

        if (room.getRoomStatus() != GameStatus.WAITING) {
            throw new IllegalStateException("이미 게임이 진행 중이거나 종료된 방입니다.");
        }

        gameRepository.updateRoomStatus(roomId, "ROLE_CHECK");

        Instant finishTime = Instant.now().plusSeconds(10);

        gameBroadcaster.broadcastPhase(roomId, "ROLE_CHECK", finishTime);

        // this가 아니라 self를 통해 호출해야 트랜잭션이 걸림
        taskScheduler.schedule(() -> self.startRunawayPhase(roomId), finishTime);
    }

    @Transactional
    public void startRunawayPhase(Long roomId) {
        int runawayLimit = gameRepository.getRunawayLimit(roomId);

        gameRepository.updateRoomStatus(roomId, "RUNAWAY");

        Instant finishTime = Instant.now().plusSeconds(runawayLimit);

        gameBroadcaster.broadcastPhase(roomId, "RUNAWAY", finishTime);

        taskScheduler.schedule(() -> self.startMainGame(roomId), finishTime);
    }

    @Transactional
    public void startMainGame(Long roomId) {
        int timeLimit = gameRepository.getTimeLimit(roomId); // 예: 600초

        gameRepository.updateRoomStatus(roomId, "PLAYING");

        Instant finishTime = Instant.now().plusSeconds(timeLimit);

        // 메인 게임은 다음 스케줄(게임 종료)이 필요하다면 여기에 추가
        gameBroadcaster. broadcastPhase(roomId, "PLAYING", finishTime);

        taskScheduler.schedule(() -> self.timeOver(roomId), finishTime);
    }

    @Transactional
    public void timeOver(Long roomId) {
        GameStatus status = gameRepository.getRoomStatus(roomId);
        if (status == GameStatus.FINISHED) return; // 이미 경찰이 이겨서 끝났으면 무시
        finishGame(roomId, Role.THIEF);
    }

    @Transactional
    public void checkGameOverCondition(Long roomId) {
        List<Player> aliveThieves = gameRepository.getAliveThieves(roomId);
        if (aliveThieves.isEmpty()) {
            finishGame(roomId, Role.POLICE);
        }
    }

    private void finishGame(Long roomId, Role winnerTeam) {
        gameRepository.updateRoomStatus(roomId, "FINISHED");
        GameResultResponse result = GameResultResponse.builder()
                .winnerTeam(winnerTeam)
                .build();
        gameBroadcaster.sendToRoom(roomId, GameMessageType.GAME_OVER, result);
    }
}
