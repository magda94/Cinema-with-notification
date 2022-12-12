package com.cinema.service.ticket.service;

import com.cinema.service.ticket.dto.TicketDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    public List<TicketDto> getAllTickets() {
        return List.of();
    }

    public TicketDto getTicketWithId(UUID ticketUuid) {
        return null;
    }

    public List<TicketDto> getTicketsForFilm(int filmId) {
        return List.of();
    }

    public TicketDto addTicket(TicketDto ticketDto) {
        return null;
    }

    public TicketDto updateTicket(UUID ticketUuid, TicketDto changedTicket) {
        return null;
    }

    public void deleteTicketWithId(UUID ticketUuid) {

    }

    public void deleteTicketsForFilm(int filmId) {

    }
}
