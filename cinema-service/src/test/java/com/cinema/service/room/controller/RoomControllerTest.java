package com.cinema.service.room.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.container.PostgresqlContainer;
import com.cinema.service.room.dto.RoomDto;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.room.utils.RoomDtoUtils;
import com.cinema.service.room.utils.RoomEntityUtils;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.show.utils.ShowEntityUtils;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.Place;
import com.cinema.service.ticket.entity.TicketEntity;
import com.cinema.service.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class RoomControllerTest extends PostgresqlContainer {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllRooms() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();

        roomRepository.save(room);

        //WHEN-THEN
        mockMvc.perform(get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].roomId", equalTo(room.getRoomId())));
    }

    @Test
    public void shouldReturnRoomWithID() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();

        roomRepository.save(room);

        //WHEN-THEN
        mockMvc.perform(get("/rooms/" + room.getRoomId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId", equalTo(room.getRoomId())))
                .andExpect(jsonPath("$.rowsNumber", equalTo(room.getRowsNumber())))
                .andExpect(jsonPath("$.columnsNumber", equalTo(room.getColumnsNumber())));
    }

    @Test
    public void shouldReturnNotFoundWhenRoomNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(get("/rooms/" + Any.intValue())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnExtendDataAboutRoom() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);
        showRepository.save(show);

        int reservedNumber = 0;
        for (int col=1; col <= room.getColumnsNumber(); col++) {
            for (int row=1; row <= room.getRowsNumber(); row++) {
                boolean reserved = new Random().nextBoolean();
                reservedNumber += reserved ? 1 : 0;
                var newTicket = TicketEntity.builder()
                        .show(show)
                        .uuid(UUID.randomUUID())
                        .filmId(show.getFilmId())
                        .place(new Place(room.getRoomId(), row, col))
                        .status(reserved ? TicketStatus.PAID : TicketStatus.FREE)
                        .build();

                ticketRepository.save(newTicket);
            }
        }

        //WHEN-THEN
        mockMvc.perform(get(String.format("/rooms/%d/tickets", room.getRoomId()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId", equalTo(room.getRoomId())))
                .andExpect(jsonPath("$.showInfo", hasSize(1)))
                .andExpect(jsonPath("$.showInfo[0].showId", equalTo(show.getShowId())))
                .andExpect(jsonPath("$.showInfo[0].tickets", hasSize(room.getColumnsNumber() * room.getRowsNumber())))
                .andExpect(jsonPath("$.showInfo[0].totalReservedNumber", equalTo(reservedNumber)));

    }

    @Test
    public void shouldReturnNotFoundWhenRoomNotExistForExtended() throws Exception {
        //WHEN-THEN
                mockMvc.perform(get(String.format("/rooms/%d/tickets", Any.intValue()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddRoom() throws Exception {
        //GIVEN
        var room = RoomDtoUtils.createRoomDto();

        //WHEN
        mockMvc.perform(post("/rooms")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(room)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId", equalTo(room.getRoomId())));

        //THEN
        assertThat(roomRepository.findByRoomId(room.getRoomId()).get().toRoomDto())
                .isEqualTo(room);
    }

    @Test
    public void shouldNotAddRoomWhenIdIsBusy() throws Exception {
        //GIVEN
        var roomEntity = RoomEntityUtils.createRoomEntity();
        roomRepository.save(roomEntity);

        var newRoom = RoomDtoUtils.createRoomDtoWithRoomId(roomEntity.getRoomId());

        //WHEN
        mockMvc.perform(post("/rooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(newRoom)))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldDeleteRoom() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);


        //WHEN
        mockMvc.perform(delete("/rooms/" + room.getRoomId()))
                .andExpect(status().isOk());

        //THEN
        assertThat(roomRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldReturnNotFoundWhenRoomNotExistToDelete() throws Exception {
        //WHEN-THEN
        mockMvc.perform(delete("/rooms/" + Any.intValue()))
                .andExpect(status().isNotFound());
    }

    private String asJson(RoomDto roomDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(roomDto);
    }

}