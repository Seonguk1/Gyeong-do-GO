//package com.project.gyeong_do_go.room.repository;
//
//import com.project.gyeong_do_go.room.entity.Room;
//import org.springframework.stereotype.Repository;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class InMemoryRoomRepository implements RoomRepository {
//
//    private final Map<String, Room> roomsById = new ConcurrentHashMap<>();
//    private final Map<String, String> roomIdByCode = new ConcurrentHashMap<>();
//
//    @Override
//    public Room findById(String roomId) {
//        return roomsById.get(roomId);
//    }
//
//    @Override
//    public String findRoomIdByCode(String joinCode) {
//        return roomIdByCode.get(joinCode);
//    }
//
//    @Override
//    public void save(Room room) {
//        roomsById.put(room.getRoomId(), room);
//    }
//
//    @Override
//    public void saveCodeMapping(String joinCode, String roomId) {
//        roomIdByCode.put(joinCode, roomId);
//    }
//
//    @Override
//    public boolean existsCode(String joinCode) {
//        return roomIdByCode.containsKey(joinCode);
//    }
//}
