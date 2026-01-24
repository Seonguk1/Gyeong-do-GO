package com.project.gyeong_do_go.room.service;

import com.project.gyeong_do_go.room.domain.RoomStatus;
import com.project.gyeong_do_go.room.dto.response.PlayerDto;
import com.project.gyeong_do_go.room.dto.response.RoomSnapshotDto;
import com.project.gyeong_do_go.room.entity.Player;
import com.project.gyeong_do_go.room.entity.Room;
import com.project.gyeong_do_go.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public Room createRoom(String nickname) {
        String roomId = UUID.randomUUID().toString();
        String hostPlayerId = UUID.randomUUID().toString();
        String code = generateUniqueJoinCode();

        Room room = new Room(roomId, code, hostPlayerId);
        Player host = new Player(hostPlayerId, normalizeNickname(nickname));
        room.addPlayer(host);

        return roomRepository.save(room);
    }

    @Transactional
    public JoinResult joinRoom(String joinCode, String nickname) {
        Room room = roomRepository.findWithPlayersByJoinCode(normalizeJoinCode(joinCode))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방 코드를 찾을 수 없습니다."));

        if (room.getStatus() != RoomStatus.LOBBY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 게임이 시작된 방입니다.");
        }

        String playerId = UUID.randomUUID().toString();
        Player p = new Player(playerId, normalizeNickname(nickname));
        room.addPlayer(p);

        roomRepository.save(room);
        return new JoinResult(room.getRoomId(), playerId);
    }

    @Transactional(readOnly = true)
    public Room getRoomWithPlayers(String roomId) {
        return roomRepository.findWithPlayersByRoomId(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방이 존재하지 않습니다."));
    }

    @Transactional
    public void setReady(String roomId, String playerId, boolean ready) {
        Room room = getRoomWithPlayers(roomId);

        if (room.getStatus() != RoomStatus.LOBBY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "로비 상태에서만 변경할 수 있습니다.");
        }

        Player player = room.getPlayers().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "플레이어가 존재하지 않습니다."));

        player.setReady(ready);
    }

    @Transactional
    public void startGame(String roomId, String requesterPlayerId) {
        Room room = getRoomWithPlayers(roomId);

        if (!room.getHostPlayerId().equals(requesterPlayerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "방장만 시작할 수 있습니다.");
        }
        if (room.getStatus() != RoomStatus.LOBBY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 게임이 시작되었습니다.");
        }

        room.setStatus(RoomStatus.IN_GAME);
    }

    @Transactional(readOnly = true)
    public RoomSnapshotDto getSnapshot(String roomId) {
        Room room = getRoomWithPlayers(roomId);

        List<PlayerDto> players = room.getPlayers().stream()
                .map(p -> new PlayerDto(p.getPlayerId(), p.getNickname(), p.isReady()))
                .toList();

        return new RoomSnapshotDto(
                room.getRoomId(),
                room.getJoinCode(),
                room.getHostPlayerId(),
                room.getStatus().name(),
                players
        );
    }

    private String generateUniqueJoinCode() {
        for (int i = 0; i < 1000; i++) {
            String code = String.format("%06d", random.nextInt(1_000_000));
            if (!roomRepository.existsByJoinCode(code)) return code;
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "방 코드 생성 실패");
    }

    private String normalizeNickname(String nickname) {
        if (nickname == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임이 필요합니다.");
        String n = nickname.trim();
        if (n.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임이 비어있습니다.");
        if (n.length() > 20) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임이 너무 깁니다.");
        return n;
    }

    private String normalizeJoinCode(String joinCode) {
        if (joinCode == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "방 코드가 필요합니다.");
        String c = joinCode.trim();
        if (!c.matches("\\d{6}")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "방 코드는 6자리 숫자여야 합니다.");
        return c;
    }

    public static class JoinResult {
        public final String roomId;
        public final String playerId;

        public JoinResult(String roomId, String playerId) {
            this.roomId = roomId;
            this.playerId = playerId;
        }
    }
}
