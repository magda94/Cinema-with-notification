package com.cinema.service.ticket.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.TicketEntity;

import java.util.UUID;

public class TicketEntityUtils {

    public static TicketEntity createTicketEntity() {
        return TicketEntity.builder()
                .uuid(UUID.randomUUID())
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .build();
    }

    public static TicketEntity createTicketEntityWithFilmId(int filmId) {
        return TicketEntity.builder()
                .uuid(UUID.randomUUID())
                .filmId(filmId)
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .build();
    }

    public static TicketEntity createTicketEntityWithUuid(UUID uuid) {
        return TicketEntity.builder()
                .uuid(uuid)
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .build();
    }
}
