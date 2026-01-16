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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자가 필수임. 다만, 무분별한 생성을 막기 위해 PROTECTED로 설정
@EntityListeners(AuditingEntityListener.class) // 생성 시간을 자동으로 기록하기 위해 필요함
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String roomCode;

    @Column(nullable = false)
    private String title;

    private int maxPlayers;

    @Enumerated(EnumType.STRING) // Enum의 '이름(문자열)' 자체를 DB에 저장 (숫자로 저장하면 나중에 순서 바뀔 때 꼬임)
    private GameMode mode;

    private Integer seekerCount; // 특정 모드에서만 사용하므로 null 허용

    @Column(nullable = false)
    private String hostId;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @CreatedDate // 데이터가 저장되는 순간 시간을 자동으로 입력함
    @Column(updatable = false) // 생성 시간은 수정되면 안 되니까 보호함
    private LocalDateTime createdAt;

    // --- 생성자 ---
    // 빌더 패턴을 사용하여 방 객체를 안전하게 생성함
    @Builder
    public Room(String roomCode, String title, int maxPlayers, GameMode mode,
                Integer seekerCount, String hostId) {
        this.roomCode = roomCode;
        this.title = title;
        this.maxPlayers = maxPlayers;
        this.mode = mode;
        this.seekerCount = seekerCount;
        this.hostId = hostId;
        this.status = RoomStatus.WAITING; // 방이 처음 만들어지면 항상 '대기 중' 상태여야 함
    }
}
