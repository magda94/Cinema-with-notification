package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.entity.ShowEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ShowEntityUtils {

    public static ShowEntity createShowEntityWithRoom(RoomEntity room) {
        var start = Any.instant();
        return ShowEntity.builder()
                .showId(Any.intValue())
                .filmId(Any.intValue())
                .room(room)
                .startDate(start)
                .endDate(start.plus(Any.intValue(), ChronoUnit.MINUTES))
                .build();
    }

    public static ShowEntity createShowEntity(int showId, RoomEntity room) {
        var start = Any.instant();
        return ShowEntity.builder()
                .showId(showId)
                .filmId(Any.intValue())
                .room(room)
                .startDate(start)
                .endDate(start.plus(Any.intValue(), ChronoUnit.MINUTES))
                .build();
    }
}
