package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 이 인터페이스가 데이터 접근 계층임을 선언함
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomCode(String roomCode);
    Optional<Room> findByRoomCode(String roomCode);
}