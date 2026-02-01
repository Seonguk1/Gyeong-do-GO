package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.PlayerStatus;
import com.project.gyeong_do_go.room.domain.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players")
public class Player extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private String nickname;

    private String socketId; // 소켓 연결 시 업데이트

    @Column(nullable = false)
    private boolean isHost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // THIEF, POLICE

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerStatus status; // ALIVE, JAILED

    private boolean isReady;

    @Builder
    public Player(Room room, String nickname, boolean isHost) {
        this.room = room;
        this.nickname = nickname;
        this.isHost = isHost;
        this.role = Role.THIEF;        // 기본값 도둑
        this.status = PlayerStatus.ALIVE; // 기본값 생존
        this.isReady = false;          // 기본값 준비 안됨
    }

    // === 비즈니스 로직 ===
    public void setStatus(PlayerStatus status) {this.status = status;}

    public void updateRole(Role newRole) {
        this.role = newRole;
    }

    public void toggleReady(boolean isReady) {
        this.isReady = isReady;
    }

    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateSocketId(String socketId) {
        this.socketId = socketId;
    }

    public void catchThief() {
        this.status = PlayerStatus.JAILED;
    }

    public void release() {
        this.status = PlayerStatus.ALIVE;
    }
}