package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.room.dto.RoomCreateRequest;
import com.project.gyeong_do_go.room.dto.RoomCreateResponse;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RoomService {
    // 가독성을 위해 숫자 0, 1과 알파벳 O, I는 제외
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom(); // Math.random()보다 안전

    public RoomCreateResponse createRoom(RoomCreateRequest request) {
        String roomCode = generateRandomCode();
        String hostId = "user_8812"; // 임시

        // DB에 방 정보를 저장함
        // saveToDatabase(roomCode, request);

        return RoomCreateResponse.builder()
                .roomCode(roomCode)
                .hostId(hostId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String generateRandomCode() {
        return IntStream.range(0, CODE_LENGTH)
                .map(i -> CHARACTERS.charAt(random.nextInt(CHARACTERS.length())))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }
}
