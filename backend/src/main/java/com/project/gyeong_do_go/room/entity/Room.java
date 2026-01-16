package com.project.gyeong_do_go.room.entity;

import com.project.gyeong_do_go.room.domain.GameMode;
import com.project.gyeong_do_go.room.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity // 이 클래스를 DB 테이블과 매핑하겠다고 선언함
@Table(name = "rooms") // DB에 생성될 테이블 이름을 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 생성을 막기 위해 PROTECTED로 설정
@EntityListeners(AuditingEntityListener.class) // 생성 시간을 자동으로 기록하기 위해 필요함
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String roomCode;

    @Column(nullable = false)
    private String title;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private GameMode mode;

    private Integer seekerCount;

    @Column(nullable = false)
    private String hostNickname;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @CreatedDate // 데이터가 저장되는 순간 시간을 자동으로 입력
    @Column(updatable = false) // 생성 시간은 수정되면 안 되니까 보호함
    private LocalDateTime createdAt;

    @Builder
    public Room(String roomCode, String title, int capacity, GameMode mode,
                Integer seekerCount, String hostNickname) {
        this.roomCode = roomCode;
        this.title = title;
        this.capacity = capacity;
        this.mode = mode;
        this.seekerCount = seekerCount;
        this.hostNickname = hostNickname;
        this.status = RoomStatus.WAITING;
    }
}
