package com.cinema.service.show.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.container.PostgresqlContainer;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.room.utils.RoomEntityUtils;
import com.cinema.service.show.dto.ShowDto;
import com.cinema.service.show.repository.ShowRepository;
import com.cinema.service.show.utils.ShowDtoUtils;
import com.cinema.service.show.utils.ShowEntityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
class ShowControllerTest extends PostgresqlContainer {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
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

        //WHEN
        mockMvc.perform(post("/shows")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(newShow)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.showId", equalTo(newShow.getShowId())));

        //THEN
        assertThat(showRepository.existsByShowId(newShow.getShowId())).isTrue();
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

    private String asJson(ShowDto showDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(showDto);
    }
}