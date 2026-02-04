package com.project.gyeong_do_go.player.repository;

import com.project.gyeong_do_go.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // 특정 방에 있는 모든 플레이어 찾기 (필요 시 사용)
     List<Player> findByRoomId(Long roomId);
}