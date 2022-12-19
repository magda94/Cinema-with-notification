package com.cinema.service.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservationResponse {

    private UUID reservationId;

    private final ReservationStatus status = ReservationStatus.CANCELLED;
}
