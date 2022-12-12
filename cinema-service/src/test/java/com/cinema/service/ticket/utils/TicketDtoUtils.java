package com.cinema.service.ticket.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.dto.TicketDto;

import java.util.UUID;

public class TicketDtoUtils {

    public static TicketDto createTicketDto() {
        return TicketDto.builder()
                .uuid(UUID.randomUUID())
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .build();
    }

    public static TicketDto createTicketDtoWithUuid(UUID uuid) {
        return TicketDto.builder()
                .uuid(uuid)
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .build();
    }
}
