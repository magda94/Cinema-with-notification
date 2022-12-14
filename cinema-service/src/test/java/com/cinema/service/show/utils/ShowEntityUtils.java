package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.entity.ShowEntity;

public class ShowEntityUtils {

    public static ShowEntity createShowEntityWithRoom(RoomEntity room) {
        return ShowEntity.builder()
                .showId(Any.intValue())
                .filmId(Any.intValue())
                .room(room)
                .startDate(Any.instant())
                .build();
    }

    public static ShowEntity createShowEntity(int showId, RoomEntity room) {
        return ShowEntity.builder()
                .showId(showId)
                .filmId(Any.intValue())
                .room(room)
                .startDate(Any.instant())
                .build();
    }
}
