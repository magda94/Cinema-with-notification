package com.cinema.service.room.dto;

import com.cinema.service.ticket.dto.TicketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowInfo {

    private int showId;

    private Instant startDate;

    private Instant endDate;

    private int filmId;

    private Set<TicketDto> tickets;

    private int totalReservedNumber;
}
