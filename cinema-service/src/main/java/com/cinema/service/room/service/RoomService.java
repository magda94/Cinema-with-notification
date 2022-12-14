package com.cinema.service.room.service;

import com.cinema.service.exceptions.RoomExistsException;
import com.cinema.service.exceptions.RoomNotFoundException;
import com.cinema.service.room.dto.ExtendRoomDto;
import com.cinema.service.room.dto.RoomDto;
import com.cinema.service.room.dto.ShowInfo;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.TicketEntity;
import com.cinema.service.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(RoomEntity::toRoomDto)
                .collect(Collectors.toList());
    }

    public RoomDto getRoomWithId(int roomId) {
        var room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: '{}'", roomId);
                    throw new RoomNotFoundException("Cannot find room with id: " + roomId);
                });

        return room.toRoomDto();
    }

    public ExtendRoomDto getAllDataForRoom(int roomId) {
        var room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: '{}'", roomId);
                    throw new RoomNotFoundException("Cannot find room with id: " + roomId);
                });

        return ExtendRoomDto.builder()
                .roomId(roomId)
                .showInfo(getShowInfoSet(room))
                .build();
    }

    private Set<ShowInfo> getShowInfoSet(RoomEntity room) {
        return room.getShows()
                .stream()
                .map(show -> getShowInfo(show, room))
                .collect(Collectors.toSet());
    }

    private ShowInfo getShowInfo(ShowEntity show, RoomEntity room) {
        var tickets = show.getTickets()
                .stream()
                .map(TicketEntity::toTicketDto)
                .collect(Collectors.toSet());

        var reservedNumber = tickets.stream()
                .filter(ticket -> !ticket.getStatus().equals(TicketStatus.FREE))
                .count();

        return ShowInfo.builder()
                .showId(show.getShowId())
                .startDate(show.getStartDate())
                .filmId(room.getRoomId())
                .tickets(tickets)
                .totalReservedNumber((int) reservedNumber)
                .build();
    }

    public RoomDto addRoom(RoomDto roomDto) {
        if (roomRepository.existsByRoomId(roomDto.getRoomId())) {
            log.error("Cannot add new room. Room with id: {} exists in database", roomDto.getRoomId());
            throw new RoomExistsException(String.format("Room with id: '%d' exists in database", roomDto.getRoomId()));
        }

        var savedRoom = roomRepository.save(toEntity(roomDto));


        return savedRoom.toRoomDto();
    }

    public void deleteRoom(int roomId) {
        var room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: '{}'", roomId);
                    throw new RoomNotFoundException("Cannot find room with id: " + roomId);
                });

        room.getShows()
                .stream()
                .forEach(show -> ticketRepository.deleteAllByShow(show));

        showRepository.deleteAllByRoom(room);

        roomRepository.deleteByRoomId(roomId);
    }

    private RoomEntity toEntity(RoomDto roomDto) {
        return RoomEntity.builder()
                .roomId(roomDto.getRoomId())
                .columnsNumber(roomDto.getColumnsNumber())
                .rowsNumber(roomDto.getRowsNumber())
                .build();
    }
}
