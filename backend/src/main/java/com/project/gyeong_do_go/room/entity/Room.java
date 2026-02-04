package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.global.entity.BaseTimeEntity;
import com.project.gyeong_do_go.player.entity.Player;
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
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String roomCode;

    // === 게임 설정 ===
    private double centerLat;
    private double centerLng;
    private int mapRadius;      // 기본값 300
    private int prisonRadius;   // 기본값 20
    private int timeLimit;      // 기본값 600 (10분)
    private int runawayLimit;   // 기본값 180 (3분)

    // === 상태 관리 ===
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
        this.centerLat = centerLat;
        this.centerLng = centerLon;
        this.mapRadius = mapRadius;
        this.prisonRadius = prisonRadius;
        this.timeLimit = timeLimit;
        this.runawayLimit = runawayLimit;
        this.roomStatus = GameStatus.WAITING;
    }

    // === 비즈니스 로직 ===

    public void addPlayer(Player player) {
        this.players.add(player);
        if (player.getRoom() != this) {
            player.setRoom(this);
        }
    }

    // 상태 변경 메소드
    public void startRoleCheck() {
        this.roomStatus = GameStatus.ROLE_CHECK;
        this.startedAt = LocalDateTime.now();
    }

    public void startRunaway() {
        this.roomStatus = GameStatus.RUNAWAY;
    }

    public void startMainGame() {
        this.roomStatus = GameStatus.PLAYING;
    }

    public void finishGame() {
        this.roomStatus = GameStatus.FINISHED;
    }

    public void updateSettings(Double lat, Double lng, Integer mapR, Integer prisonR, Integer time, Integer runTime) {
        if (lat != null) this.centerLat = lat;
        if (lng != null) this.centerLng = lng;
        if (mapR != null) this.mapRadius = mapR;
        if (prisonR != null) this.prisonRadius = prisonR;
        if (time != null) this.timeLimit = time;
        if (runTime != null) this.runawayLimit = runTime;
    }
}