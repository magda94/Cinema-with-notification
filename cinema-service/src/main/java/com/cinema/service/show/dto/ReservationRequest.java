package com.cinema.service.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    private String userLogin;

    private UUID ticketId;

    private int showId;

    private RequestReservationStatus status;
}
