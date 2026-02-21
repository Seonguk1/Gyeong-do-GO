package com.project.gyeong_do_go.global.config;

import com.project.gyeong_do_go.game.component.GameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

//    private final JwtUtil jwtUtil; // 토큰 검증용 (예시)
    private final GameValidator gameValidator;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 1. 메시지에서 헤더(Accessor) 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 2. CONNECT(연결) 요청일 때만 검증 수행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // A. 토큰 검증 (JWT를 쓴다면)
//            String token = accessor.getFirstNativeHeader("Authorization");
//            if (token == null || !jwtUtil.validateToken(token)) {
//                throw new CustomException(ErrorCode.UNAUTHORIZED);
//            }

            // B. 방 입장 권한 검증 (헤더에서 roomId, playerId 꺼내기)
            // 클라이언트가 connect({ "roomId": 1, "playerId": 5 }) 처럼 헤더에 담아 보내야 함
            String roomIdStr = accessor.getFirstNativeHeader("roomId");
            String playerIdStr = accessor.getFirstNativeHeader("playerId");

            if (roomIdStr != null && playerIdStr != null) {
                try {
                    Long roomId = Long.parseLong(roomIdStr);
                    Long playerId = Long.parseLong(playerIdStr);

                    gameValidator.validateAndGet(roomId, playerId);

                    // C. 검증 통과 후, 세션(SessionAttributes)에 저장
                    // 이후 요청부터는 DB 조회 없이 이 값을 꺼내 씀
                    accessor.getSessionAttributes().put("roomId", roomId);
                    accessor.getSessionAttributes().put("playerId", playerId);
                    log.info("✅ WebSocket 인증 성공: roomId={}, playerId={}", roomId, playerId);
                } catch (NumberFormatException e) {
                    log.error("❌ WebSocket 인증 실패 - 데이터 파싱 오류: roomId={}, playerId={}", roomIdStr, playerIdStr, e);
                    throw new IllegalArgumentException("roomId 또는 playerId이 숫자가 아닙니다");
                } catch (Exception e) {
                    log.error("❌ WebSocket 인증 실패: {}", e.getMessage(), e);
                    throw new RuntimeException("WebSocket 인증 실패", e);
                }
            } else {
                log.warn("⚠️ WebSocket CONNECT 시 roomId 또는 playerId 헤더 누락: roomId={}, playerId={}", roomIdStr, playerIdStr);
            }
        }

        return message; // 검증 통과 시 메시지 그대로 진행
    }
}