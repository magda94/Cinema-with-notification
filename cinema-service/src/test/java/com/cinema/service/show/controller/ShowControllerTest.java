package com.cinema.service.show.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.WireMockConfig;
import com.cinema.service.container.PostgresqlContainer;
import com.cinema.service.film.controller.utils.FilmDtoUtils;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.room.utils.RoomEntityUtils;
import com.cinema.service.show.dto.RequestReservationStatus;
import com.cinema.service.show.dto.ReservationRequest;
import com.cinema.service.show.dto.ReservationStatus;
import com.cinema.service.show.repository.ReservationRepository;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.show.utils.ReservationEntityUtils;
import com.cinema.service.show.utils.ShowDtoUtils;
import com.cinema.service.show.utils.ShowEntityUtils;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.entity.Place;
import com.cinema.service.ticket.entity.TicketEntity;
import com.cinema.service.ticket.repository.TicketRepository;
import com.cinema.service.ticket.utils.TicketEntityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoUnit;
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
@ContextConfiguration(classes = { WireMockConfig.class })
class ShowControllerTest extends PostgresqlContainer {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer mockServer;

    @BeforeEach
    public void before() {
        reservationRepository.deleteAll();
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllShows() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        //WHEN-THEN
        mockMvc.perform(get("/shows")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].showId", equalTo(show.getShowId())))
                .andExpect(jsonPath("$[0].roomId", equalTo(room.getRoomId())));
    }

    @Test
    public void shouldReturnShowWithId() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        //WHEN-THEN
        mockMvc.perform(get("/shows/" + show.getShowId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showId", equalTo(show.getShowId())))
                .andExpect(jsonPath("$.roomId", equalTo(room.getRoomId())));
    }

    @Test
    public void shouldReturnNotFoundWhenShowNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(get("/shows/" + Any.intValue())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddShow() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var newShow = ShowDtoUtils.createAnyShowDtoWithRoomId(room.getRoomId());

        var film = FilmDtoUtils.createFilmDtoWithId(newShow.getFilmId());

        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/films/" + film.getCinemaFilmId()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(asJson(film)))
        );

        //WHEN
        mockMvc.perform(post("/shows")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(newShow)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.showId", equalTo(newShow.getShowId())));

        //THEN
        assertThat(showRepository.existsByShowId(newShow.getShowId())).isTrue();
        assertThat(ticketRepository.count()).isEqualTo(room.getRowsNumber() * room.getColumnsNumber());
    }

    @Test
    public void shouldNotAddShowWhenFilmNotExist() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var newShow = ShowDtoUtils.createAnyShowDtoWithRoomId(room.getRoomId());

        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/films/" + newShow.getFilmId()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("Cannot find film"))
        );

        //WHEN
        mockMvc.perform(post("/shows")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(newShow)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", equalTo("Cannot find film")));

        //THEN
        assertThat(showRepository.existsByShowId(newShow.getShowId())).isFalse();
        assertThat(ticketRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldNotAddShowWhenSlotIsBusy() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(1, room);
        showRepository.save(show);

        var newShow = ShowDtoUtils.createShowDtoWithSlot(room, show.getStartDate().plus(10, ChronoUnit.MINUTES));

        var film = FilmDtoUtils.createFilmDtoWithId(newShow.getFilmId());

        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/films/" + film.getCinemaFilmId()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(asJson(film)))
        );

        //WHEN
        mockMvc.perform(post("/shows")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(newShow)))
                .andExpect(status().isConflict());

        //THEN
        assertThat(showRepository.count()).isEqualTo(1);

    }

    @Test
    public void shouldNotAddShowWhenShowIdIsBusy() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntity(1, room);
        showRepository.save(show);

        var newShow = ShowDtoUtils.createShowDto(show.getShowId(), room.getRoomId());

        //WHEN
        mockMvc.perform(post("/shows")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(newShow)))
                .andExpect(status().isConflict());

        //THEN
        assertThat(showRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldNotAddShowWhenRoomNotExist() throws Exception {
        //GIVEN
        var show = ShowDtoUtils.createShowDto(Any.intValue(), Any.intValue());

        //WHEN
        mockMvc.perform(post("/shows")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(show)))
                .andExpect(status().isNotFound());

        //THEN
        assertThat(showRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteShow() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        //WHEN
        mockMvc.perform(delete("/shows/" + show.getShowId()))
                .andExpect(status().isOk());

        //THEN
        assertThat(showRepository.count()).isEqualTo(0);
        assertThat(roomRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldCancelShow() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var ticket = TicketEntity.builder()
                .uuid(UUID.randomUUID())
                .filmId(show.getFilmId())
                .status(TicketStatus.FREE)
                .show(show)
                .id(Any.longValue())
                .place(Place.builder()
                        .roomId(room.getRoomId())
                        .rowNumber(Any.intValue())
                        .columnNumber(Any.intValue())
                        .build())
                .build();

        ticketRepository.save(ticket);

        //WHEN
        mockMvc.perform(post("/shows/cancel/" + show.getShowId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(showRepository.count()).isEqualTo(0);
        assertThat(ticketRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldNotCancelShowWhenNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(post("/shows/cancel/" + Any.intValue()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAllActualReservations() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var ticket1 = TicketEntityUtils.createTicketEntity(show);
        var ticket2 = TicketEntityUtils.createTicketEntity(show);
        ticketRepository.saveAll(List.of(ticket1, ticket2));

        var reservation1 = ReservationEntityUtils.createReservationEntity(show.getShowId(),
                ticket1.getUuid(), ReservationStatus.CANCELLED);
        var reservation2 = ReservationEntityUtils.createReservationEntity(show.getShowId(),
                ticket2.getUuid(), ReservationStatus.PAID);

        reservationRepository.saveAll(List.of(reservation1, reservation2));

        //WHEN-THEN
        mockMvc.perform(get("/shows/reservations/" + show.getShowId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ticketId", equalTo(reservation2.getTicketId().toString())));
    }

    @Test
    public void shouldReturnNotFoundWhenShowNotExistForReservations() throws Exception {
        //WHEN-THEN
        mockMvc.perform(get("/shows/reservations/" + Any.intValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReserveTicket() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntity(show);
        ticket.setStatus(TicketStatus.FREE);
        ticketRepository.save(ticket);

        var request = ReservationRequest.builder()
                .showId(show.getShowId())
                .status(RequestReservationStatus.RESERVED)
                .userLogin(Any.string())
                .ticketId(ticket.getUuid())
                .build();

        //WHEN
        mockMvc.perform(post("/shows/reservation")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.reservationStatus", equalTo(ReservationStatus.RESERVED.name())))
                .andExpect(jsonPath("$.ticketId", equalTo(ticket.getUuid().toString())));

        //THEN
        assertThat(reservationRepository.findAllActualByShowId(show.getShowId())).hasSize(1);
    }

    @Test
    public void shouldReturnNotFoundWhenShowNotExistForReservation() throws Exception {
        //GIVEN
        var request = ReservationRequest.builder()
                .showId(Any.intValue())
                .status(RequestReservationStatus.RESERVED)
                .userLogin(Any.string())
                .ticketId(UUID.randomUUID())
                .build();

        //WHEN-THEN
        mockMvc.perform(post("/shows/reservation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenTicketNotExistForReservation() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var request = ReservationRequest.builder()
                .showId(show.getShowId())
                .status(RequestReservationStatus.RESERVED)
                .userLogin(Any.string())
                .ticketId(UUID.randomUUID())
                .build();

        //WHEN-THEN
        mockMvc.perform(post("/shows/reservation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictWhenTicketNotMatchToShow() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show1 = ShowEntityUtils.createShowEntityWithRoom(room);
        var show2 = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.saveAll(List.of(show1, show2));

        var ticket = TicketEntityUtils.createTicketEntity(show2);
        ticket.setStatus(TicketStatus.FREE);
        ticketRepository.save(ticket);

        var request = ReservationRequest.builder()
                .showId(show1.getShowId())
                .status(RequestReservationStatus.RESERVED)
                .userLogin(Any.string())
                .ticketId(ticket.getUuid())
                .build();

        //WHEN-THEN
        mockMvc.perform(post("/shows/reservation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnConflictWhenTicketAlreadyReserved() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntity(show);
        ticket.setStatus(TicketStatus.PAID);
        ticketRepository.save(ticket);

        var request = ReservationRequest.builder()
                .showId(show.getShowId())
                .status(RequestReservationStatus.RESERVED)
                .userLogin(Any.string())
                .ticketId(ticket.getUuid())
                .build();

        //WHEN-THEN
        mockMvc.perform(post("/shows/reservation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldCancelReservation() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var ticket = TicketEntityUtils.createTicketEntity(show);
        ticketRepository.save(ticket);

        var reservation = ReservationEntityUtils.createReservationEntity(show.getShowId(),
                ticket.getUuid(), ReservationStatus.PAID);

        reservationRepository.save(reservation);
        var savedReservation = reservationRepository.findAll().get(0);

        //WHEN
        mockMvc.perform(post("/shows/reservation/cancel/" + savedReservation.getUuid())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.reservationId", equalTo(savedReservation.getUuid().toString())));

        //THEN
        assertThat(reservationRepository.findById(savedReservation.getUuid()).get().getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    public void shouldReturnNotFoundWhenReservationNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(post("/shows/reservation/cancel/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenTicketNotExistForCancelReservation() throws Exception {
        //GIVEN
        var room = RoomEntityUtils.createRoomEntity();
        roomRepository.save(room);

        var show = ShowEntityUtils.createShowEntityWithRoom(room);

        showRepository.save(show);

        var reservation = ReservationEntityUtils.createReservationEntity(show.getShowId(),
                UUID.randomUUID(), ReservationStatus.PAID);

        reservationRepository.save(reservation);
        var savedReservation = reservationRepository.findAll().get(0);

        mockMvc.perform(post("/shows/reservation/cancel/" + savedReservation.getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String asJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(object);
    }
}