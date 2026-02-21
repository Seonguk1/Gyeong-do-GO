package com.project.gyeong_do_go.global.scheduler;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.game.repository.GameRepository;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameCleanupScheduler {

    private final GameRepository gameRepository;
    private final GameBroadcaster gameBroadcaster;
    private final RoomRepository roomRepository; // 직접 써도 됨

    @Scheduled(cron = "0 0 4 * * *")
    @Transactional // 삭제 작업이 있으므로 트랜잭션 필수
    public void cleanupOldGames() {
        LocalDateTime limit = LocalDateTime.now().minusHours(24);

        // 1. 삭제할 방들을 먼저 찾음 (방송을 위해)
        List<Room> oldRooms = roomRepository.findAllByCreatedAtBefore(limit);

        for (Room room : oldRooms) {
            // 2. 방 폭파 방송 (필요하다면)
//            gameBroadcaster.sendToRoom(room.getId(), GameMessageType.GAME_OVER, "TIMEOUT");

            // 3. 삭제
            roomRepository.delete(room);
        }
    }
}