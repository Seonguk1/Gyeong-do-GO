// File: src/main/java/com/project/gyeong_do_go/room/service/RoomService.java
package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.entity.RoomMember;
import com.project.gyeong_do_go.room.domain.RoomStatus;
import com.project.gyeong_do_go.room.exception.AlreadyJoinedException;
import com.project.gyeong_do_go.room.exception.RoomFullException;
import com.project.gyeong_do_go.room.exception.RoomNotFoundException;
import com.project.gyeong_do_go.room.exception.RoomNotJoinableException;
import com.project.gyeong_do_go.room.repository.RoomMemberRepository;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    private final SecureRandom random = new SecureRandom();

    @Transactional
    public CreateRoomResult createRoom(Long hostUserId, String title, Integer capacity) {
        if (hostUserId == null) throw new IllegalArgumentException("hostUserId is null");
        int cap = (capacity == null) ? 4 : capacity;
        if (cap < 2) throw new IllegalArgumentException("capacity less than MIN");

        String code = generateUniqueCode(6);

        Room room = Room.create(code, title, hostUserId, cap);

        // Room을 먼저 DB에 반영해서 id를 확정
        Room savedRoom = roomRepository.saveAndFlush(room);

        roomMemberRepository.save(RoomMember.host(savedRoom.getId(), hostUserId));

        return new CreateRoomResult(savedRoom.getId(), savedRoom.getCode());
    }

    @Transactional
    public JoinRoomResult joinRoom(Long userId, String code) {
        if (userId == null) throw new IllegalArgumentException("userId is null");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("code is null");

        Room room = roomRepository.findByCode(code.trim().toUpperCase())
                .orElseThrow(RoomNotFoundException::new);

        if (room.getStatus() != RoomStatus.WAITING) throw new RoomNotJoinableException();
        if (roomMemberRepository.existsByRoomIdAndUserId(room.getId(), userId)) throw new AlreadyJoinedException();

        long count = roomMemberRepository.countByRoomId(room.getId());
        if (count >= room.getCapacity()) throw new RoomFullException();

        try {
            RoomMember saved = roomMemberRepository.save(RoomMember.member(room.getId(), userId));
            return new JoinRoomResult(room.getId(), saved.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyJoinedException();
        }
    }

    private String generateUniqueCode(int length) {
        for (int i = 0; i < 20; i++) {
            String code = randomCode(length);
            if (!roomRepository.existsByCode(code)) return code;
        }
        throw new IllegalStateException("failed to generate unique code");
    }

    private String randomCode(int length) {
        final char[] chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(chars[random.nextInt(chars.length)]);
        return sb.toString();
    }

    public record CreateRoomResult(Long roomId, String code) {}
    public record JoinRoomResult(Long roomId, Long memberId) {}
}
