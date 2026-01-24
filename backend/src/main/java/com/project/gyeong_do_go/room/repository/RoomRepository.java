package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {

    boolean existsByJoinCode(String joinCode);

    @EntityGraph(attributePaths = "players")
    Optional<Room> findWithPlayersByRoomId(String roomId);

    @EntityGraph(attributePaths = "players")
    Optional<Room> findWithPlayersByJoinCode(String joinCode);
}
