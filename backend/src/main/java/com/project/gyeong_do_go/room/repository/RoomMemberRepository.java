package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    long countByRoomId(Long roomId);

    List<RoomMember> findAllByRoomIdOrderByJoinedAtAsc(Long roomId);

    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);
}