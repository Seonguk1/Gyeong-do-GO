package com.project.gyeong_do_go.player.entity;

import com.project.gyeong_do_go.global.util.GeometryUtil;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "player_location", timeToLive = 3600) // 1시간 뒤 자동 삭제 (메모리 관리)
public class PlayerRedis {
    @Id
    private Long playerId; // key 역할을 함
    private Long roomId;
    private double latitude;
    private double longitude; 

    @Builder.Default
    private double totalDistance = 0.0;

    public void updatePosition(double newLat, double newLng) {
        // 1. 이동 거리 계산
        double moved = GeometryUtil.calculateDistance(this.latitude, this.longitude, newLat, newLng);

        // 2. 튀는 값(GPS Noise) 필터링
        if (moved >= 0.5 && moved <= 100.0) {
            this.totalDistance += moved; // 거리 누적
            this.latitude = newLat;           // 좌표 갱신
            this.longitude = newLng;
        }
        // 3. 필터링 걸리면? 좌표 갱신 안 함 (또는 좌표만 갱신하고 거리는 안 더함)
    }
}