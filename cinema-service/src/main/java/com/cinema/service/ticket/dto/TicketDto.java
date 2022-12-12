package com.cinema.service.ticket.dto;

import com.cinema.service.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private UUID uuid;

    private int filmId;

    private String userLogin;

    private TicketStatus status;
}
