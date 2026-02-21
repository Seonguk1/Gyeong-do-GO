package com.project.gyeong_do_go.player.repository;

import com.project.gyeong_do_go.player.entity.PlayerRedis;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRedisRepository extends CrudRepository<PlayerRedis, Long> {
    // findById, save, delete 등 기본 기능 자동 제공!
}