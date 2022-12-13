package com.cinema.service.room.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.room.entity.RoomEntity;

import java.util.Random;

public class RoomEntityUtils {

    public static RoomEntity createRoomEntity() {
        return RoomEntity.builder()
                .rowsNumber(new Random().nextInt(1, 10))
                .columnsNumber(new Random().nextInt(1, 10))
                .roomId(Any.intValue())
                .build();
    }
}
