package com.project.gyeong_do_go.room.repository;

import com.project.gyeong_do_go.room.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByCode(String code);
    boolean existsByCode(String code);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select r from Room r where r.code = :code")
//    Optional<Room> findByCodeForUpdate(@Param("code") String code);
}