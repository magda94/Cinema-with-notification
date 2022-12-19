package com.cinema.service.show.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.show.dto.ReservationStatus;
import com.cinema.service.show.entity.ReservationEntity;

import java.util.UUID;

public class ReservationEntityUtils {

    public static ReservationEntity createReservationEntity(int showId, UUID ticketId, ReservationStatus status) {
        return ReservationEntity.builder()
                .showId(showId)
                .userLogin(Any.string())
                .ticketId(ticketId)
                .uuid(UUID.randomUUID())
                .status(status)
                .build();
    }
}
