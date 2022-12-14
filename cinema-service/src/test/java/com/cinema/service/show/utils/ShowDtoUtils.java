package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.show.dto.ShowDto;

public class ShowDtoUtils {

    public static ShowDto createAnyShowDtoWithRoomId(int roomId) {
        return ShowDto.builder()
                .showId(Any.intValue())
                .roomId(roomId)
                .filmId(Any.intValue())
                .startDate(Any.instant())
                .build();
    }

    public static ShowDto createShowDto(int showId, int roomId) {
        return ShowDto.builder()
                .showId(showId)
                .roomId(roomId)
                .filmId(Any.intValue())
                .startDate(Any.instant())
                .build();
    }
}
