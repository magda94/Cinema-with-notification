package com.cinema.service.ticket.dto;

import com.cinema.service.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    @NotNull
    private UUID uuid;

    @NotNull
    private int filmId;

    @NotNull
    private int showId;

    private PlaceDto place;

    private String userLogin;

    @NotNull
    private TicketStatus status;
}
