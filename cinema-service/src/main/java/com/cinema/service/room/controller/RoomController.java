package com.cinema.service.room.controller;

import com.cinema.service.room.dto.ExtendRoomDto;
import com.cinema.service.room.dto.RoomDto;
import com.cinema.service.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @GetMapping("")
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        log.info("Get all rooms in the cinema request");
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomWithId(@PathVariable("roomId") int roomId) {
        log.info("Get room with id: '{}' request", roomId);
        return ResponseEntity.ok(roomService.getRoomWithId(roomId));
    }

    @GetMapping("/{roomId}/seats")
    public ResponseEntity<ExtendRoomDto> getAllDataForRoom(@PathVariable("roomId") int roomId) {
        log.info("Get all information about room with id: '{}' request", roomId);
        return ResponseEntity.ok(roomService.getAllDataForRoom(roomId));
    }

    @PostMapping("")
    public ResponseEntity<RoomDto> addRoom(@RequestBody @Valid RoomDto roomDto) {
        log.info("Add room with id: '{}' request", roomDto.getRoomId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.addRoom(roomDto));
    }

    //TODO: delete room
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable("roomId") int roomId) {
        log.info("Delete room with id: '{}' request");
        roomService.deleteRoom(roomId);
    }
}
