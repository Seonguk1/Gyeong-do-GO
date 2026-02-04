package com.project.gyeong_do_go.player.entity;

import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.global.entity.BaseTimeEntity;
import com.project.gyeong_do_go.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players")
public class Player extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    // === 게임 내 정보 ===
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerStatus status; // ALIVE, JAILED, OUT

    @Column(nullable = false)
    private boolean isReady;

    @Column(nullable = false)
    private boolean isHost;

    private double latitude;
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public Player(Room room, String nickname, boolean isHost) {
        this.room = room;
        this.nickname = nickname;
        this.isHost = isHost;
        this.role = Role.THIEF;        // 기본값 도둑
        this.status = PlayerStatus.ALIVE; // 기본값 생존
        this.isReady = false;          // 기본값 준비 안됨
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    // === 비즈니스 로직 ===
    public void updateStatus(PlayerStatus status) {
        this.status = status;
    }

    // 2. 더 좋은 방식 (행위를 나타냄)
    public void arrest() {
        this.status = PlayerStatus.OUT;
    }

    public void rescue() {
        this.status = PlayerStatus.ALIVE;
    }

    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void catchThief() {
        this.status = PlayerStatus.JAILED;
    }

    public void release() {
        this.status = PlayerStatus.ALIVE;
    }
}