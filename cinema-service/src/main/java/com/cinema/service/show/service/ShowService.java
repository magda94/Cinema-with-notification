package com.cinema.service.show.service;

import com.cinema.service.exceptions.*;
import com.cinema.service.film.client.FilmServiceClient;
import com.cinema.service.film.dto.FilmDto;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.show.dto.*;
import com.cinema.service.show.entity.ReservationEntity;
import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.show.repository.ReservationRepository;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.Place;
import com.cinema.service.ticket.entity.TicketEntity;
import com.cinema.service.ticket.repository.TicketRepository;
import com.cinema.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowService {

    private final static int BUFFER_MINUTES = 15;

    private final ShowRepository showRepository;
    private final RoomRepository roomRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    private final FilmServiceClient filmServiceClient;

    private final UserService userService;

    public List<ResponseShowDto> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(ShowEntity::toResponseShowDto)
                .collect(Collectors.toList());
    }

    public ResponseShowDto getShowWithId(int showId) {
        return showRepository.findByShowId(showId)
                .map(ShowEntity::toResponseShowDto)
                .orElseThrow(() -> {
                    log.error("Cannot find show with id: {}", showId);
                    throw new ShowNotFoundException("Cannot find show with id: " + showId);
                });
    }

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
    public ResponseShowDto addNewShow(RequestShowDto requestShowDto) {
        if (showRepository.existsByShowId(requestShowDto.getShowId())) {
            log.error("Cannot add new show. Show with id: '{}' exists in database", requestShowDto.getShowId());
            throw new ShowExistsException(String.format("Show with id: %d exists in database", requestShowDto.getShowId()));
        }

        var room = roomRepository.findByRoomId(requestShowDto.getRoomId())
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: {}", requestShowDto.getRoomId());
                    throw new ShowNotFoundException("Cannot find room with id: " + requestShowDto.getRoomId());
                });

        var film = filmServiceClient.getFilmWithId(requestShowDto.getFilmId());

        var newShow = toEntity(requestShowDto, room, film);

        if (showRepository.findOtherShow(room.getRoomId(), newShow.getStartDate(), newShow.getEndDate()).size() != 0) {
            log.error("Cannot add show with id: {}. Timeslot is occupied.", newShow.getShowId());
            throw new ShowCollisionException("Timeslot is occupied");
        }

        var savedShow = showRepository.save(newShow);

        createAndSaveTicketsForShow(savedShow, room);

        return savedShow.toResponseShowDto();
    }

    @Transactional
    public void cancelShow(int showId) {
        var show = showRepository.findByShowId(showId)
                .orElseThrow(() -> {
                    log.error("Cannot find show with id: {}", showId);
                    throw new ShowNotFoundException("Cannot find show with id: " + showId);
                });

        log.info("Delete tickets for show with id: '{}'", showId);
        ticketRepository.deleteAllByShow(show);

        showRepository.deleteByShowId(showId);
    }

    public void deleteShowWithId(int showId) {
        showRepository.findByShowId(showId)
                        .ifPresent(ticketRepository::deleteAllByShow);

        showRepository.deleteByShowId(showId);
    }

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

    private ShowEntity toEntity(RequestShowDto requestShowDto, RoomEntity room, FilmDto filmDto) {
        return ShowEntity.builder()
                .showId(requestShowDto.getShowId())
                .filmId(requestShowDto.getFilmId())
                .startDate(requestShowDto.getStartDate())
                .endDate(requestShowDto.getStartDate().plus(filmDto.getDuration() + BUFFER_MINUTES, ChronoUnit.MINUTES))
                .room(room)
                .build();
    }

    private void createAndSaveTicketsForShow(ShowEntity show, RoomEntity room) {
        for (int row = 1; row <= room.getRowsNumber(); row++) {
            for (int col = 1; col <= room.getColumnsNumber(); col++) {
                var newTicket = TicketEntity.builder()
                        .uuid(UUID.randomUUID())
                        .filmId(show.getFilmId())
                        .show(show)
                        .status(TicketStatus.FREE)
                        .place(Place.builder()
                                .roomId(room.getRoomId())
                                .rowNumber(row)
                                .columnNumber(col)
                                .build())
                        .build();

                ticketRepository.save(newTicket);
            }
        }
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
