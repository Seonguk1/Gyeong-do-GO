package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.common.exception.BusinessException;
import com.project.gyeong_do_go.common.exception.ErrorCode;
import com.project.gyeong_do_go.room.dto.RoomCreateRequest;
import com.project.gyeong_do_go.room.dto.RoomCreateResponse;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional // 데이터 무결성
public class RoomService {
    private final RoomRepository roomRepository;
    // 가독성을 위해 숫자 0, 1과 알파벳 O, I는 제외
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom(); // Math.random()보다 안전

    public RoomCreateResponse createRoom(RoomCreateRequest request) {
        String roomCode = generateUniqueRoomCode();

        Room room = Room.builder()
                .roomCode(roomCode)
                .title(request.getTitle())
                .maxPlayers(request.getMaxPlayers())
                .mode(request.getMode())
                .seekerCount(request.getDetails() != null ? request.getDetails().getSeekerCount() : null)
                .hostId("user_8812") // 지금은 임시, 나중엔 인증 정보에서 가져옴
                .build();

        Room savedRoom = roomRepository.save(room);

        return RoomCreateResponse.builder()
                .roomCode(savedRoom.getRoomCode())
                .hostId(savedRoom.getHostId())
                .createdAt(savedRoom.getCreatedAt())
                .build();
    }

    private String generateUniqueRoomCode() {
        String code;
        int retryCount = 0;

        while (true) {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            code = sb.toString();

            if (!roomRepository.existsByRoomCode(code)) { // 중복 체크
                return code;
            }

            if (++retryCount > 100) {
                throw new BusinessException(ErrorCode.SERVER_ERROR);
            }
        }
    }
}
