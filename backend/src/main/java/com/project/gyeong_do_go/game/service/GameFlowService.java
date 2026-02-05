package com.project.gyeong_do_go.game.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.component.GameReader;
import com.project.gyeong_do_go.game.domain.GameMessageType;
import com.project.gyeong_do_go.game.dto.response.GameResultResponse;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRedisRepository;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.domain.GameStatus;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameFlowService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final RoomRepository roomRepository;
    private final GameBroadcaster gameBroadcaster;
    private final GameReader  gameReader;
    private final TaskScheduler taskScheduler;

    @Getter
    @AllArgsConstructor
    public class MvpResult {
        private String nickname;
        private String reason;
    }

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

        if (playerRepository.existsByRoomIdAndIsReadyFalse(roomId)) {
            throw new IllegalStateException("모든 플레이어가 준비 상태가 되어야 합니다.");
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

    @Transactional
    public void finishGame(Long roomId, Role winnerTeam) {
        List<Player> players = gameRepository.getPlayers(roomId);

        for (Player p : players) {
            // Redis에서 최종 위치/이동거리 조회
            playerRedisRepository.findById(p.getId()).ifPresent(redisData -> {

                double finalDistance = redisData.getTotalDistance();
                p.addDistance(finalDistance);

                // Redis 데이터 삭제
                playerRedisRepository.delete(redisData);
            });
        }

        MvpResult mvp = calculateMvp(roomId, winnerTeam);

        gameRepository.updateRoomStatus(roomId, "FINISHED");
        GameResultResponse result = GameResultResponse.builder()
                .winnerTeam(winnerTeam)
                .mvpPlayer(mvp.getNickname())
                .mvpReason(mvp.getReason())
                .build();
        gameBroadcaster.sendToRoom(roomId, GameMessageType.GAME_OVER, result);
    }

    private MvpResult calculateMvp(Long roomId, Role winnerTeam) {
        List<Player> players = gameRepository.getPlayers(roomId);

        Player mvp = null;
        String reason = "";

        if (winnerTeam == Role.POLICE) {
            // 경찰 승리 시: 가장 많이 잡은 사람
            mvp = players.stream()
                    .filter(p -> p.getRole() == Role.POLICE)
                    .max(Comparator.comparingInt(Player::getCatchCount)) // catchCount 최대값
                    .orElse(null);
            if (mvp != null) reason = "총 " + mvp.getCatchCount() + "명 검거";
        } else {
            // 도둑 승리 시: 가장 많이 구했거나, 끝까지 살아남은 사람
            // (여기서는 구출 횟수 우선으로 예시)
            mvp = players.stream()
                    .filter(p -> p.getRole() == Role.THIEF)
                    .max(Comparator.comparingInt(Player::getRescueCount)
                            .thenComparingDouble(Player::getTotalDistance)) // 동점이면 뛴 거리 순
                    .orElse(null);
            if (mvp != null) reason = "동료 " + mvp.getRescueCount() + "명 구출";
        }

        // null 처리 등은 생략
        return new MvpResult(mvp.getNickname(), reason);
    }

    @Transactional
    public void restartGame(Long playerId) {
        Player host = gameReader.getPlayer(playerId);
        if (!host.isHost()) {
            throw new IllegalStateException("방장만 재시작을 할 수 있습니다.");
        }

        Room room = host.getRoom();
        Long roomId = room.getId();

        // 2. 방 상태 검증 (FINISHED 상태일 때만 가능)
        if (room.getRoomStatus() != GameStatus.FINISHED) {
            throw new IllegalStateException("게임이 종료된 상태에서만 재시작 가능합니다.");
        }
        room.updateStatus(GameStatus.WAITING);
        room.setStartedAt(null); // 시작 시간 초기화

        List<Player> players = room.getPlayers();
        List<Long> playerIds = new ArrayList<>(); // Redis 삭제용 ID 모음

        for (Player p : players) {
            p.setReady(false);
            p.setStatus(PlayerStatus.ALIVE);
            p.updateLocation(0.0, 0.0);
            playerIds.add(p.getId());
        }

        playerRedisRepository.deleteAllById(playerIds);

        // 만약 쿨타임 등을 별도 키로 관리했다면 그것도 삭제
        // redisTemplate.delete("catch_cooldown:" + playerId); ...

        gameBroadcaster.broadcastRoomInfo(roomId);
    }
}
