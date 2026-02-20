package com.project.gyeong_do_go.game.component;

import com.project.gyeong_do_go.global.entity.RoomAndPlayer;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameValidator {

    private final GameReader gameReader; // or Repository

    /**
     * 통합 검증 메서드
     * - REST: PathVariable의 roomId, playerId 사용
     * - WebSocket: SessionAttributes의 roomId, playerId 사용
     */
    public RoomAndPlayer validateAndGet(Long roomId, Long playerId) {
        // 1. 플레이어 조회 (DB)
        Player player = gameReader.getPlayer(playerId);

        // 2. 룸 조회 (DB - 필요하다면. 보통 player.getRoom()으로 충분하지만, 확실히 하려면 조회)
        Room room = gameReader.getRoom(roomId);

        // 3. 관계 검증 (핵심)
        // 플레이어가 속한 방이 없거나, 요청 들어온 roomId와 다르면 에러
        if (player.getRoom() == null || !player.getRoom().getId().equals(roomId)) {
            throw new CustomException(ErrorCode.PLAYER_NOT_IN_ROOM);
        }

        // 4. 검증 통과 시 객체 반환
        return new RoomAndPlayer(room, player);
    }
}