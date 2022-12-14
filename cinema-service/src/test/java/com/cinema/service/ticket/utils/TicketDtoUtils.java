package com.cinema.service.ticket.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.dto.PlaceDto;
import com.cinema.service.ticket.dto.TicketDto;

import java.util.UUID;

public class TicketDtoUtils {

    public static TicketDto createTicketDto(int showId) {
        return TicketDto.builder()
                .uuid(UUID.randomUUID())
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .place(createAnyPlaceDto())
                .showId(showId)
                .build();
    }

    public static TicketDto createTicketDtoWithUuid(UUID uuid, int showId) {
        return TicketDto.builder()
                .uuid(uuid)
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .place(createAnyPlaceDto())
                .showId(showId)
                .build();
    }

    private static PlaceDto createAnyPlaceDto() {
        return PlaceDto.builder()
                .roomId(Any.intValue())
                .columnNumber(Any.intValue())
                .rowNumber(Any.intValue())
                .build();
    }
}
