package com.cinema.service.show.service;

import com.cinema.service.exceptions.ShowNotFoundException;
import com.cinema.service.exceptions.TicketMismatchException;
import com.cinema.service.exceptions.TicketNotFoundException;
import com.cinema.service.exceptions.TicketReservedException;
import com.cinema.service.show.dto.*;
import com.cinema.service.show.entity.ReservationEntity;
import com.cinema.service.show.repository.ReservationRepository;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;

    public List<ReservationResponse> getReservationsForShow(int showId) {
        if (!showRepository.existsByShowId(showId)) {
            log.error("Cannot find show with id: {}", showId);
            throw new ShowNotFoundException("Cannot find show with id: " + showId);
        }

        return reservationRepository.findAllActualByShowId(showId)
                .stream()
                .map(this::toReservationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse reserveTicketForShow(ReservationRequest request) {
        var show = showRepository.findByShowId(request.getShowId())
                .orElseThrow(() -> {
                    log.error("Cannot find show with id: {}", request.getShowId());
                    throw new ShowNotFoundException("Cannot find show with id: " + request.getShowId());
                });

        //TODO: check if user exist

        var ticket = ticketRepository.findByUuid(request.getTicketId())
                .orElseThrow(() -> {
                    log.error("Cannot find ticket with id: {}", request.getTicketId());
                    throw new TicketNotFoundException("Cannot find ticket with id: " + request.getTicketId());
                });

        if (!ticket.getShow().equals(show)) {
            log.info("Ticket with id: '{}' doesn't match to show with id: '{}'", ticket.getUuid(), show.getShowId());
            throw new TicketMismatchException(String.format("Ticket with id: '%s' mismatch to show: '%d'",
                    ticket.getUuid(), show.getShowId()));
        }

        if (ticket.getStatus() != TicketStatus.FREE) {
            log.info("Ticket with id: '{}' has already reserved");
            throw new TicketReservedException(String.format("Ticket with id '%s' has already reserved", ticket.getUuid()));
        }

        var reservation = ReservationEntity.builder()
                .ticketId(ticket.getUuid())
                .status(toReservationStatus(request.getStatus()))
                .userLogin(request.getUserLogin())
                .showId(request.getShowId())
                .build();

        reservationRepository.save(reservation);

        ticket.setStatus(request.getStatus() == RequestReservationStatus.RESERVED ?
                TicketStatus.RESERVED : TicketStatus.PAID);
        ticket.setUserLogin(request.getUserLogin());

        ticketRepository.save(ticket);

        return ReservationResponse.builder()
                .ticketId(ticket.getUuid())
                .userLogin(request.getUserLogin())
                .reservationId(reservation.getUuid())
                .place(ticket.getPlace())
                .reservationStatus(reservation.getStatus())
                .showId(reservation.getShowId())
                .build();
    }

    @Transactional
    public CancelReservationResponse cancelReservation(UUID reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    log.error("Cannot find reservation with id: {}", reservationId);
                    throw new TicketNotFoundException("Cannot find reservation with id: " + reservationId);
                });

        var ticket = ticketRepository.findByUuid(reservation.getTicketId())
                .orElseThrow(() -> {
                    log.error("Cannot find ticket with id: {}", reservation.getTicketId());
                    throw new TicketNotFoundException("Cannot find ticket with id: " + reservation.getTicketId());
                });

        ticket.setUserLogin(null);
        ticket.setStatus(TicketStatus.FREE);

        log.info("Free ticket with id: '{}", ticket.getUuid());
        ticketRepository.save(ticket);

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);

        return CancelReservationResponse.builder()
                .reservationId(reservation.getUuid())
                .build();
    }

    private ReservationStatus toReservationStatus(RequestReservationStatus reservationStatus) {
        return switch (reservationStatus) {
            case RESERVED -> ReservationStatus.RESERVED;
            case PAID -> ReservationStatus.PAID;
        };
    }

    private ReservationResponse toReservationResponse(ReservationEntity entity) {
        var ticket = ticketRepository.findByUuid(entity.getTicketId())
                .orElseThrow(() -> {
                    log.error("Cannot find ticket with id: {}", entity.getTicketId());
                    throw new TicketNotFoundException("Cannot find ticket with id: " + entity.getTicketId());
                });

        return ReservationResponse.builder()
                .reservationId(entity.getUuid())
                .reservationStatus(entity.getStatus())
                .ticketId(entity.getTicketId())
                .place(ticket.getPlace())
                .userLogin(entity.getUserLogin())
                .showId(entity.getShowId())
                .build();
    }
}
