package com.cinema.service.room.service;

import com.cinema.service.room.dto.ExtendRoomDto;
import com.cinema.service.room.dto.RoomDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    public List<RoomDto> getAllRooms() {
        return List.of();
    }

    public RoomDto getRoomWithId(int roomId) {
        return null;
    }

    public ExtendRoomDto getAllDataForRoom(int roomId) {
        return null;
    }

    public RoomDto addRoom(RoomDto roomDto) {
        return null;
    }

    public void deleteRoom(int roomId) {

    }
}
