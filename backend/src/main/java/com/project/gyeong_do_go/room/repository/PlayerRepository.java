package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByRoom_Id(Long roomId);
}
