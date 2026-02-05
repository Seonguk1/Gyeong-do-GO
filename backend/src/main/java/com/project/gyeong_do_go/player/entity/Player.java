package com.project.gyeong_do_go.player.entity;

import com.project.gyeong_do_go.global.entity.BaseTimeEntity;
import com.project.gyeong_do_go.player.domain.PlayerStatus;
import com.project.gyeong_do_go.player.domain.Role;
import com.project.gyeong_do_go.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String prisonerNumber;

    private double latitude;
    private double longitude;

    // === [MVP 산정용 통계 필드] ===

    private int catchCount = 0;      // (경찰) 도둑 잡은 횟수

    private int rescueCount = 0;     // (도둑) 동료 구해준 횟수

    private double totalDistance = 0.0; // (공통) 총 이동 거리 (미터)

    private LocalDateTime caughtAt;  // (도둑) 잡힌 시간 (생존 시간 계산용)

    private LocalDateTime gameJoinedAt; // (공통) 게임 참가 시간 (또는 방 생성 시점 활용)

    // ============================

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

    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 경찰이 도둑 잡았을 때
    public void increaseCatchCount() {
        this.catchCount++;
    }

    // 도둑이 동료 구했을 때
    public void increaseRescueCount(int count) {
        this.rescueCount += count;
    }

    // 이동했을 때 (기존 좌표와 새 좌표 사이 거리 누적)
    public void addDistance(double distanceInMeters) {
        this.totalDistance += distanceInMeters;
    }

    // 잡혔을 때 (생존 시간 계산 종료점)
    public void markAsCaught() {
        this.status = PlayerStatus.JAILED;
        this.caughtAt = LocalDateTime.now();
    }

    public void rescue() {
        this.status = PlayerStatus.ALIVE;
    }
}