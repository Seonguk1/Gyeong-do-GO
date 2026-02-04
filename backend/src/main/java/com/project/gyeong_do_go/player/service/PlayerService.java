package com.project.gyeong_do_go.player.service;

import com.project.gyeong_do_go.game.component.GameBroadcaster;
import com.project.gyeong_do_go.global.error.CustomException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.player.entity.Player;
import com.project.gyeong_do_go.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {
    private PlayerRepository playerRepository;
    private final GameBroadcaster gameBroadcaster;

    @Transactional
    public void updatePlayerRole(Long roomId, Long playerId, Role role) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));
        // 유효성 검사 (이 플레이어가 진짜 그 방에 있는지?)
        if (!player.getRoom().getId().equals(roomId)) {
            throw new IllegalArgumentException("잘못된 방 요청입니다.");
        }
        player.setRole(role);
        gameBroadcaster.broadcastRoomInfo(roomId);
    }

    @Transactional
    public void updatePlayerReady(Long roomId, Long playerId, boolean isReady) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));
        if (!player.getRoom().getId().equals(roomId)) {
            throw new IllegalArgumentException("잘못된 방 요청입니다.");
        }
        player.setReady(isReady);
        gameBroadcaster.broadcastRoomInfo(roomId);
    }
}
