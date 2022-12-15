package com.cinema.service.ticket.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.Place;
import com.cinema.service.ticket.entity.TicketEntity;

import java.util.UUID;

public class TicketEntityUtils {

    public static TicketEntity createTicketEntity(ShowEntity show) {
        return TicketEntity.builder()
                .uuid(UUID.randomUUID())
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .place(createAnyPlace())
                .status(Any.of(TicketStatus.class))
                .show(show)
                .build();
    }

    public static TicketEntity createTicketEntityWithFilmId(int filmId, ShowEntity show) {
        return TicketEntity.builder()
                .uuid(UUID.randomUUID())
                .filmId(filmId)
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .place(createAnyPlace())
                .show(show)
                .build();
    }

    public static TicketEntity createTicketEntityWithUuid(UUID uuid, ShowEntity show) {
        return TicketEntity.builder()
                .uuid(uuid)
                .filmId(Any.intValue())
                .userLogin(Any.string())
                .status(Any.of(TicketStatus.class))
                .place(createAnyPlace())
                .show(show)
                .build();
    }

    private static Place createAnyPlace() {
        return Place.builder()
                .roomId(Any.intValue())
                .columnNumber(Any.intValue())
                .rowNumber(Any.intValue())
                .build();
    }
}
