package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="host_id", nullable = true)
    private Player host;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Embedded
    private RoomSettings settings;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setHost(Player host) {
        this.host = host;
    }

    public Room(String code, RoomSettings settings) {
        this.code = code;
        this.host = null;
        this.status = RoomStatus.LOBBY;
        this.settings = settings;
    }

    public Room(String code, Player host, RoomSettings settings) {
        this.code = code;
        this.host = host;
        this.status = RoomStatus.LOBBY;
        this.settings = settings;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void updateSettings(RoomSettings newSettings) {
        if (this.status != RoomStatus.LOBBY) {
            throw new IllegalStateException("로비 상태에서만 설정 변경이 가능합니다.");
        }
        this.settings = newSettings;
    }

    public void start() {
        if (this.status != RoomStatus.LOBBY) {
            throw new IllegalStateException("이미 시작되었거나 종료된 방입니다.");
        }
        this.status = RoomStatus.IN_GAME;
    }
}
