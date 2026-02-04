package com.project.gyeong_do_go.game.component;

import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.global.util.GeometryUtil;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameValidator {
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;

    public void validateDistance(Player p1, Player p2, double limit) {
        double dist = GeometryUtil.calculateDistance(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
        if (dist > limit) {
            throw new CustomException(ErrorCode.DISTANCE_TOO_FAR);
        }
    }
}
