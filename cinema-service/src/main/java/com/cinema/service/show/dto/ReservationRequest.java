package com.cinema.service.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    @NotNull
    private String userLogin;

    @NotNull
    private UUID ticketId;

    @NotNull
    private int showId;

    @NotNull
    private RequestReservationStatus status;
}
