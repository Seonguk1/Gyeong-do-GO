package com.project.gyeong_do_go.room.dto.response;

import com.project.gyeong_do_go.room.domain.Team;
import com.project.gyeong_do_go.room.entity.RoomSettings;

import java.util.List;

public record RoomStateResponse (String code, RoomSettings settings, int playerCount, List<PlayerInfo> infos){
    public record PlayerInfo (String nickname, Team team) {}
}