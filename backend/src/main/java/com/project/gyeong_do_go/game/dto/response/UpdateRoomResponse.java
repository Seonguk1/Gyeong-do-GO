package com.project.gyeong_do_go.game.dto.response;

import com.project.gyeong_do_go.room.domain.GameStatus;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class UpdateRoomResponse {
    private Long roomId;
    private String roomCode;
    private String roomStatus;

    private double centerLat;
    private double centerLon;
    private int mapRadius;
    private int prisonRadius;
    private int timeLimit;
    private int runawayLimit;

    private List<PlayerInfo> players;

    @Getter
    @Builder
    public static class PlayerInfo {
        private Long id;
        private String nickname;
        private String role;
        private boolean isReady;
    }
}
