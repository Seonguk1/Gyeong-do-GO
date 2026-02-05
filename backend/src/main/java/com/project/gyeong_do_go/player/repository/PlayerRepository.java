package com.project.gyeong_do_go.player.repository;

import com.project.gyeong_do_go.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // 특정 방에 있는 모든 플레이어 찾기 (필요 시 사용)
     List<Player> findByRoomId(Long roomId);

    @Query("SELECT p FROM Player p WHERE p.room.id = :roomId AND p.role = 'THIEF' AND p.status = 'OUT'")
    List<Player> findPrisonersByRoomId(@Param("roomId") Long roomId);

    boolean existsByRoomIdAndIsReadyFalse(Long roomId);

    Optional<Player> findByRoomIdAndPrisonerNumber(Long roomId, String prisonerNumber);
}