package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.dto.RequestShowDto;

import java.time.Instant;

public class ShowDtoUtils {

    public static RequestShowDto createAnyShowDtoWithRoomId(int roomId) {
        return RequestShowDto.builder()
                .showId(Any.intValue())
                .roomId(roomId)
                .filmId(Any.intValue())
                .startDate(Any.instant())
                .build();
    }

    public static RequestShowDto createShowDto(int showId, int roomId) {
        return RequestShowDto.builder()
                .showId(showId)
                .roomId(roomId)
                .filmId(Any.intValue())
                .startDate(Any.instant())
                .build();
    }

    public static RequestShowDto createShowDtoWithSlot(RoomEntity room, Instant from) {
        return RequestShowDto.builder()
                .showId(Any.intValue())
                .filmId(Any.intValue())
                .roomId(room.getRoomId())
                .startDate(from)
                .build();
    }
}
