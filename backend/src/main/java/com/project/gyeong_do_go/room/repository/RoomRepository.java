package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomCode(String roomCode);

    @Query("SELECT r FROM Room r JOIN FETCH r.players WHERE r.id = :roomId")
    Optional<Room> findByIdWithPlayers(@Param("roomId") Long roomId);

    void deleteByCreatedAtBefore(LocalDateTime dateTime);
    List<Room> findAllByCreatedAtBefore(LocalDateTime dateTime);
}