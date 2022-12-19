package com.cinema.service.show.service;

import com.cinema.service.exceptions.*;
import com.cinema.service.film.client.FilmServiceClient;
import com.cinema.service.film.dto.FilmDto;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.show.dto.*;
import com.cinema.service.show.entity.ShowEntity;
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
}
