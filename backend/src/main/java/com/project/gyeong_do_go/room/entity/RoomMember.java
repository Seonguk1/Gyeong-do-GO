// File: src/main/java/com/project/gyeong_do_go/room/entity/RoomMember.java
package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.RoomMemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "room_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_room_member_room_user", columnNames = {"room_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_room_member_room_id", columnList = "room_id"),
                @Index(name = "idx_room_member_user_id", columnList = "user_id")
        }
)
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private RoomMemberRole role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public static RoomMember host(Long roomId, Long userId) {
        return create(roomId, userId, RoomMemberRole.HOST);
    }

    public static RoomMember member(Long roomId, Long userId) {
        return create(roomId, userId, RoomMemberRole.MEMBER);
    }

    private static RoomMember create(Long roomId, Long userId, RoomMemberRole role) {
        RoomMember rm = new RoomMember();
        rm.roomId = roomId;
        rm.userId = userId;
        rm.role = role;
        return rm;
    }

    @PrePersist
    void onCreate() {
        this.joinedAt = LocalDateTime.now();
        if (this.role == null) this.role = RoomMemberRole.MEMBER;
    }
}
