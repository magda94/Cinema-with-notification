package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.show.dto.RequestShowDto;

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
}
