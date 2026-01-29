package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // 특정 방에 있는 모든 플레이어 찾기 (필요 시 사용)
    // List<Player> findByRoomId(Long roomId); -> JPA 기본 기능으로 커버 가능하지만 명시 가능
}