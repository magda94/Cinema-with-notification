package com.cinema.service.room.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.room.dto.RoomDto;

import java.util.Random;

public class RoomDtoUtils {

    public static RoomDto createRoomDto() {
        return RoomDto.builder()
                .roomId(Any.intValue())
                .rowsNumber(new Random().nextInt(1, 10))
                .columnsNumber(new Random().nextInt(1, 10))
                .build();
    }

    public static RoomDto createRoomDtoWithRoomId(int roomId) {
        return RoomDto.builder()
                .roomId(roomId)
                .rowsNumber(new Random().nextInt(1, 10))
                .columnsNumber(new Random().nextInt(1, 10))
                .build();
    }
}
