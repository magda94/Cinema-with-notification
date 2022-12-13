package com.cinema.service.ticket.controller;

import com.cinema.service.ticket.dto.TicketDto;
import com.cinema.service.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("")
    public ResponseEntity<List<TicketDto>> getTickets() {
        log.info("Get all tickets request");
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{ticketUuid}")
    public ResponseEntity<TicketDto> getTicketWithId(@PathVariable("ticketUuid") UUID ticketUuid) {
        log.info("Get ticket with id '{}' request", ticketUuid);
        return ResponseEntity.ok(ticketService.getTicketWithId(ticketUuid));
    }

    @GetMapping("/film/{filmId}")
    public ResponseEntity<List<TicketDto>> getTicketsForFilm(@PathVariable("filmId") int filmId) {
        log.info("Get all tickets for film with id: '{}' request", filmId);
        return ResponseEntity.ok(ticketService.getTicketsForFilm(filmId));
    }

    @PostMapping("")
    public ResponseEntity<TicketDto> addTicket(@RequestBody @Valid TicketDto ticketDto) {
        log.info("Add ticket with id: '{}' request", ticketDto.getUuid());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.addTicket(ticketDto));
    }

    @PutMapping("/{ticketUuid}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable("ticketUuid") UUID ticketUuid, @RequestBody @Valid TicketDto ticketDto) {
        log.info("Update ticket with id: '{}' request", ticketUuid);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ticketService.updateTicket(ticketUuid, ticketDto));
    }

    @DeleteMapping("/{ticketUuid}")
    public void deleteTicketWithId(@PathVariable("ticketUuid") UUID ticketUuid) {
        log.info("Delete ticket with id: '{}' request", ticketUuid);
        ticketService.deleteTicketWithId(ticketUuid);
    }

    @DeleteMapping("/film/{filmId}")
    public void deleteTicketsForFilm(@PathVariable("filmId") int filmId) {
        log.info("Delete tickets for film with id: '{}' requests", filmId);
        ticketService.deleteTicketsForFilm(filmId);
    }
}
