package com.cinema.service.ticket.service;

import com.cinema.service.exceptions.TicketExistsException;
import com.cinema.service.exceptions.TicketNotFoundException;
import com.cinema.service.ticket.dto.TicketDto;
import com.cinema.service.ticket.entity.TicketEntity;
import com.cinema.service.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketEntity::toTicketDto)
                .collect(Collectors.toList());
    }

    public TicketDto getTicketWithId(UUID ticketUuid) {
        return ticketRepository.findByUuid(ticketUuid)
                .map(TicketEntity::toTicketDto)
                .orElseThrow(() -> {
                    log.error("Cannot find ticket with id: {}", ticketUuid);
                    throw new TicketNotFoundException("Cannot find ticket with id: " + ticketUuid);
                });
    }

    public List<TicketDto> getTicketsForFilm(int filmId) {
        return ticketRepository.findAllByFilmId(filmId)
                .stream()
                .map(TicketEntity::toTicketDto)
                .collect(Collectors.toList());
    }

    public TicketDto addTicket(TicketDto ticketDto) {
        if (ticketRepository.existsByUuid(ticketDto.getUuid())) {
            log.error("Cannot add ticket. Ticket with uuid: {} exists in database", ticketDto.getUuid());
            throw new TicketExistsException(String.format("Ticket with uuid: '%s' exists in database", ticketDto.getUuid()));
        }

        return ticketRepository.save(toEntity(ticketDto))
                .toTicketDto();
    }

    public TicketDto updateTicket(UUID ticketUuid, TicketDto changedTicket) {
        if (ticketRepository.existsByUuid(changedTicket.getUuid()) && !ticketUuid.equals(changedTicket.getUuid())) {
            log.error("Cannot add ticket. Ticket with uuid: {} exists in database", changedTicket.getUuid());
            throw new TicketExistsException(String.format("Ticket with uuid: '%s' exists in database", changedTicket.getUuid()));
        }

        TicketEntity ticket = ticketRepository.findByUuid(ticketUuid)
                .orElseThrow(() -> {
                        log.error("Cannot find ticket with id: ", ticketUuid);
                        throw new TicketNotFoundException("Cannot find ticket with id: " + ticketUuid);
                });

        TicketEntity newTicket = toEntity(changedTicket);
        newTicket.setId(ticket.getId());

        return ticketRepository.save(newTicket)
                .toTicketDto();
    }

    public void deleteTicketWithId(UUID ticketUuid) {
        ticketRepository.deleteByUuid(ticketUuid);
    }

    public void deleteTicketsForFilm(int filmId) {
        ticketRepository.deleteAllByFilmId(filmId);
    }

    private TicketEntity toEntity(TicketDto ticketDto) {
        return TicketEntity.builder()
                .uuid(ticketDto.getUuid())
                .filmId(ticketDto.getFilmId())
                .userLogin(ticketDto.getUserLogin())
                .status(ticketDto.getStatus())
                .build();
    }
}