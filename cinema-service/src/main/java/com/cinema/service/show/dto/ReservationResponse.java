package com.cinema.service.show.dto;

import com.cinema.service.ticket.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    private UUID reservationId;

    private ReservationStatus reservationStatus;

    private String userLogin;

    private UUID ticketId;

    private Place place;
}
