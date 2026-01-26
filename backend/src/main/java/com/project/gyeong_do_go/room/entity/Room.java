package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "rooms",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rooms_join_code", columnNames = "join_code")
        }
)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room {
    @Id
    @Column(name = "room_id", length = 36)
    private String roomId;

    @Column(name = "join_code", length = 6, nullable = false)
    private String joinCode;

    @Column(name = "host_player_id", length = 36, nullable = false)
    private String hostPlayerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private RoomStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("joinedAt ASC")
    private List<Player> players = new ArrayList<>();

    public Room(String roomId, String joinCode, String hostPlayerId) {
        this.roomId = roomId;
        this.joinCode = joinCode;
        this.hostPlayerId = hostPlayerId;
        this.status = RoomStatus.LOBBY;
        this.createdAt = Instant.now();
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setRoom(this);
    }
}
