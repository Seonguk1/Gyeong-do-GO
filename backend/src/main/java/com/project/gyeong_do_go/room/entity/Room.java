package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.GameStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rooms")
public class Room extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String roomCode;

    // 맵 설정
    private double centerLat;
    private double centerLon;
    private int mapRadius;      // 기본값 300
    private int prisonRadius;   // 기본값 20

    // 시간 설정 (초 단위)
    private int timeLimit;      // 기본값 600 (10분)
    private int runawayLimit;   // 기본값 180 (3분)

    @Enumerated(EnumType.STRING)
    private GameStatus roomStatus;

    private LocalDateTime startedAt; // 게임(RoleCheck) 시작 시각

    // Player와 1:N 관계 (방이 삭제되면 플레이어도 삭제)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @Builder
    public Room(String roomCode, double centerLat, double centerLon,
                int mapRadius, int prisonRadius, int timeLimit, int runawayLimit) {
        this.roomCode = roomCode;
        this.roomStatus = GameStatus.WAITING;
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.mapRadius = mapRadius;
        this.prisonRadius = prisonRadius;
        this.timeLimit = timeLimit;
        this.runawayLimit = runawayLimit;
    }

    // === 비즈니스 로직 ===

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // 1. 게임 시작 (역할 확인 단계)
    public void startRoleCheck() {
        this.roomStatus = GameStatus.ROLE_CHECK;
        this.startedAt = LocalDateTime.now();
    }

    // 2. 도주 시작
    public void startRunaway() {
        this.roomStatus = GameStatus.RUNAWAY;
    }

    // 3. 본게임 시작
    public void startMainGame() {
        this.roomStatus = GameStatus.PLAYING;
    }

    // 4. 게임 종료
    public void finishGame() {
        this.roomStatus = GameStatus.FINISHED;
    }
}