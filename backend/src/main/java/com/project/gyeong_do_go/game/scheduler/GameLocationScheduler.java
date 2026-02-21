package com.project.gyeong_do_go.game.scheduler;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.room.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameLocationScheduler {
    private final GameRepository gameRepository;
    private final GameBroadcaster gameBroadcaster;

    /**
     * 진행 중인 모든 게임 방의 플레이어 위치 스냅샷을 일정 주기마다 전송
     * 기본값: 1초 (1000ms)
     */
    @Scheduled(fixedRate = 1000)
    public void broadcastLocationSnapshots() {
        List<Room> playingRooms = gameRepository.getPlayingRooms();
        for (Room room : playingRooms) {
            gameBroadcaster.broadcastLocationSnapshot(room.getId());
        }
    }
}
