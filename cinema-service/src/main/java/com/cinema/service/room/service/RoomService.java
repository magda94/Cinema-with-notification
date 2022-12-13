package com.cinema.service.room.service;

import com.cinema.service.exceptions.RoomExistsException;
import com.cinema.service.exceptions.RoomNotFoundException;
import com.cinema.service.room.dto.ExtendRoomDto;
import com.cinema.service.room.dto.RoomDto;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.entity.SeatEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.room.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

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

        var seats = seatRepository.findByRoom(room)
                .stream()
                .map(SeatEntity::toSeatDto)
                .collect(Collectors.toSet());

        return ExtendRoomDto.builder()
                .roomId(roomId)
                .seats(seats)
                .totalReservedNumber(seatRepository.countByRoomAndReserved(room, true))
                .build();
    }

    public RoomDto addRoom(RoomDto roomDto) {
        if (roomRepository.existsByRoomId(roomDto.getRoomId())) {
            log.error("Cannot add new room. Room with id: {} exists in database", roomDto.getRoomId());
            throw new RoomExistsException(String.format("Room with id: '%d' exists in database", roomDto.getRoomId()));
        }

        var savedRoom = roomRepository.save(toEntity(roomDto));

        for (int row = 1; row <= roomDto.getRowsNumber(); row++) {
            for (int col = 1; col <= roomDto.getColumnsNumber(); col++) {
                var newSeat = SeatEntity.builder()
                        .room(savedRoom)
                        .columnNumber(col)
                        .rowNumber(row)
                        .reserved(false)
                        .build();

                seatRepository.save(newSeat);
            }
        }

        return savedRoom.toRoomDto();
    }

    public void deleteRoom(int roomId) {
        var room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: '{}'", roomId);
                    throw new RoomNotFoundException("Cannot find room with id: " + roomId);
                });
        seatRepository.deleteAllByRoom(room);

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
