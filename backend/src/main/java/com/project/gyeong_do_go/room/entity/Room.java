// File: src/main/java/com/project/gyeong_do_go/room/entity/Room.java
package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "room",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_room_code", columnNames = "code")
        },
        indexes = {
                @Index(name = "idx_room_host_user_id", columnList = "host_user_id"),
                @Index(name = "idx_room_status", columnList = "status")
        }
)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = true, length = 8)
    private String code;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "host_user_id", nullable = true)
    private Long hostUserId;

    @Column(name = "capacity", nullable = true)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true, length = 20)
    private RoomStatus status;

    @Version
    @Column(name = "version", nullable = true)
    private Long version;

    @Column(name = "created_at", nullable = true, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    public static Room create(String code, String title, Long hostUserId, Integer capacity) {
        Room room = new Room();
        room.code = code;
        room.title = title;
        room.hostUserId = hostUserId;
        room.capacity = capacity == null ? 4 : capacity;
        room.status = RoomStatus.WAITING;
        room.version = 0L;
        return room;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.capacity == null) this.capacity = 4;
        if (this.status == null) this.status = RoomStatus.WAITING;
        if (this.version == null) this.version = 0L;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void changeStatus(RoomStatus status) {
        this.status = status;
    }
}
