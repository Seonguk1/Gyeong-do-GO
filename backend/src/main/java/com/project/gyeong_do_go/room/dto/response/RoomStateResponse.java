package com.project.gyeong_do_go.room.dto.response;

import com.project.gyeong_do_go.room.domain.Team;

import java.util.List;

public record RoomStateResponse (String code, int playerCount, List<PlayerInfo> infos){
    public record PlayerInfo (String nickname, Team team) {}
}