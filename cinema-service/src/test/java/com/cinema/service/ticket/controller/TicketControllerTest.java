package com.cinema.service.ticket.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.container.PostgresqlContainer;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.room.utils.RoomEntityUtils;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.show.utils.ShowEntityUtils;
import com.cinema.service.ticket.dto.TicketDto;
import com.cinema.service.ticket.repository.TicketRepository;
import com.cinema.service.ticket.utils.TicketDtoUtils;
import com.cinema.service.ticket.utils.TicketEntityUtils;
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

import java.util.List;
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
class TicketControllerTest extends PostgresqlContainer {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void before() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void shouldReturnEmptyListWhenNoTicket() throws Exception {
        //WHEN-THEN
        mockMvc.perform(get("/tickets")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    public void shouldGetAllFilms() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntity(show);
        ticketRepository.save(ticket);

        //WHEN-THEN
        mockMvc.perform(get("/tickets")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].uuid", equalTo(ticket.getUuid().toString())));
    }

    @Test
    public void shouldGetTicketWithId() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket1 = TicketEntityUtils.createTicketEntity(show);
        var ticket2 = TicketEntityUtils.createTicketEntity(show);

        ticketRepository.saveAll(List.of(ticket1, ticket2));

        //WHEN-THEN
        mockMvc.perform(get("/tickets/" + ticket1.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.uuid", equalTo(ticket1.getUuid().toString())))
                .andExpect(jsonPath("$.filmId", equalTo(ticket1.getFilmId())))
                .andExpect(jsonPath("$.status", equalTo(ticket1.getStatus().toString())));
    }

    @Test
    public void shouldReturnNotFoundWhenTicketWithIdNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(get("/tickets/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnTicketsForFilm() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket1 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);
        var ticket2 = TicketEntityUtils.createTicketEntityWithFilmId(2, show);
        var ticket3 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);

        ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3));

        //WHEN-THEN
        mockMvc.perform(get("/tickets/film/" + ticket1.getFilmId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uuid", equalTo(ticket1.getUuid().toString())))
                .andExpect(jsonPath("$[1].uuid", equalTo(ticket3.getUuid().toString())));
    }

    @Test
    public void shouldAddTicket() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket = TicketDtoUtils.createTicketDto(show.getShowId());

        //WHEN
        mockMvc.perform(post("/tickets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(ticket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid", equalTo(ticket.getUuid().toString())));

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldNotAddTicketWhenUuidIsBusy() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntityWithUuid(UUID.randomUUID(), show);
        ticketRepository.save(ticket);

        var newTicket = TicketDtoUtils.createTicketDtoWithUuid(ticket.getUuid(), show.getShowId());

        //WHEN
        mockMvc.perform(post("/tickets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(newTicket)))
                .andExpect(status().isConflict());

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldUpdateTicket() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntityWithFilmId(1, show);
        ticketRepository.save(ticket);

        var changedTicket = TicketDtoUtils.createTicketDtoWithUuid(ticket.getUuid(), show.getShowId());
        changedTicket.setFilmId(2);

        //WHEN
        mockMvc.perform(put("/tickets/" + ticket.getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(changedTicket)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.filmId", equalTo(changedTicket.getFilmId())));

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(1);
        assertThat(ticketRepository.findByUuid(ticket.getUuid()).get().toTicketDto())
                .isEqualTo(changedTicket);
    }

    @Test
    public void shouldNotUpdateTicketWhenNotExist() throws Exception {
        //GIVEN
        var ticket = TicketDtoUtils.createTicketDto(Any.intValue());

        //WHEN
        mockMvc.perform(put("/tickets/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(ticket)))
                .andExpect(status().isNotFound());

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldNotUpdateTicketWhenUuidIsBusy() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket1 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);
        var ticket2 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);

        ticketRepository.saveAll(List.of(ticket1, ticket2));

        var changedTicket = TicketDtoUtils.createTicketDtoWithUuid(ticket2.getUuid(), show.getShowId());
        changedTicket.setFilmId(2);

        //WHEN
        mockMvc.perform(put("/tickets/" + ticket1.getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(changedTicket)))
                .andExpect(status().isConflict());

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(2);
        assertThat(ticketRepository.findByUuid(ticket1.getUuid()).get())
                .isEqualTo(ticket1);
    }

    @Test
    public void shouldDeleteFilmWithId() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntity(show);

        //WHEN
        mockMvc.perform(delete("/tickets/" + ticket.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteAllTicketsForFilm() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(Any.intValue(), room);
        showRepository.save(show);

        var ticket1 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);
        var ticket2 = TicketEntityUtils.createTicketEntityWithFilmId(2, show);
        var ticket3 = TicketEntityUtils.createTicketEntityWithFilmId(1, show);
        var ticket4 = TicketEntityUtils.createTicketEntityWithFilmId(2, show);

        ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3, ticket4));

        //WHEN
        mockMvc.perform(delete("/tickets/film/" + ticket1.getFilmId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(ticketRepository.count()).isEqualTo(2);
        assertThat(ticketRepository.findAllByFilmId(ticket1.getFilmId())).hasSize(0);
    }

    private String asJson(TicketDto ticketDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ticketDto);
    }
}