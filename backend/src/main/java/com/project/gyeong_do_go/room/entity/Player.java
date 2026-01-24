package com.project.gyeong_do_go.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "players")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Player {
    @Id
    @Column(name = "player_id", length = 36)
    private String playerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_players_room"))
    private Room room;

    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "ready", nullable = false)
    private boolean ready;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    public Player(String playerId, String nickname) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.ready = false;
        this.joinedAt = Instant.now();
    }

    void setRoom(Room room) {
        this.room = room;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
