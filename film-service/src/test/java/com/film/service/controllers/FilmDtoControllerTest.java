package com.film.service.controllers;

import autofixture.publicinterface.Any;
import com.film.service.container.MongoDbContainer;
import com.film.service.dto.FilmDto;
import com.film.service.repository.FilmRepository;
import com.film.service.utils.FilmDocumentUtils;
import com.film.service.utils.FilmDtoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.List;

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
class FilmDtoControllerTest extends MongoDbContainer {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        filmRepository.deleteAll();
    }

    @Test
    public void shouldReturnEmptyListWhenNoFilmAdded() throws Exception {
        //WHEN
        var result = mockMvc.perform( MockMvcRequestBuilders
                        .get("/films")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void shouldReturnFilmList() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();

        filmRepository.saveAll(List.of(film1, film2));

        //WHEN-THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/films")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo(film1.getName())))
                .andExpect(jsonPath("$[1].name", equalTo(film2.getName())));
    }

    @Test
    public void shouldReturnFilmForId() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();

        filmRepository.saveAll(List.of(film1, film2));

        //WHEN-THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + film1.getCinemaFilmId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(film1.getName())))
                .andExpect(jsonPath("$.cinemaFilmId", equalTo(film1.getCinemaFilmId())));
    }

    @Test
    public void shouldReturnFilmForDirector() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();
        filmRepository.saveAll(List.of(film1, film2));

        //WHEN-THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/films/director")
                .accept(MediaType.APPLICATION_JSON)
                .param("name", film1.getDirector().getName())
                .param("lastName", film1.getDirector().getLastName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", equalTo(film1.getName())));
    }

    @Test
    public void shouldAddFilm() throws Exception {
        //GIVEN
        var filmDto = FilmDtoUtils.createFilmDto();

        //WHEN
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(filmDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(filmDto.getName())));


        //THEN
        var result = filmRepository.findByCinemaFilmId(filmDto.getCinemaFilmId());
        assertThat(result.isPresent());
        assertThat(result.get().toDto()).isEqualTo(filmDto);
    }

    @Test
    public void shouldNotAddFilmWhenIdIsBusy() throws Exception {
        //GIVEN
        var filmDocument = FilmDocumentUtils.createFilmDocument();
        filmRepository.save(filmDocument);

        var filmDto = FilmDtoUtils.createFilmDtoWithId(filmDocument.getCinemaFilmId());

        //WHEN
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(filmDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        //THEN
        assertThat(filmRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldUpdateFilm() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();
        filmRepository.save(film);

        var changedFilm = FilmDtoUtils.createFilmDtoWithId(film.getCinemaFilmId());

        //WHEN
        mockMvc.perform(put("/films/" + film.getCinemaFilmId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(changedFilm))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name", equalTo(changedFilm.getName())));

        //THEN
        var result = filmRepository.findByCinemaFilmId(film.getCinemaFilmId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toDto()).isEqualTo(changedFilm);
    }

    @Test
    public void shouldReturnNotFoundWhenFilmNotExist() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();
        filmRepository.save(film);

        var changedFilm = FilmDtoUtils.createFilmDtoWithId(film.getCinemaFilmId());

        //WHEN
        mockMvc.perform(put("/films/" + Any.intValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(changedFilm))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictWhenIdIsBusy() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();
        filmRepository.saveAll(List.of(film1, film2));

        var changedFilm = FilmDtoUtils.createFilmDtoWithId(film2.getCinemaFilmId());

        //WHEN
        mockMvc.perform(put("/films/" + film1.getCinemaFilmId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(changedFilm))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        //THEN
        assertThat(filmRepository.findAll()).isEqualTo(List.of(film1, film2));
    }

    @Test
    public void shouldDeleteFilm() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();
        filmRepository.save(film);

        //WHEN
        mockMvc.perform(delete("/films/" + film.getCinemaFilmId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(filmRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteFilmForDirector() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();
        filmRepository.saveAll(List.of(film1, film2));

        //WHEN
        mockMvc.perform(delete("/films/director")
                .accept(MediaType.APPLICATION_JSON)
                .param("name", film2.getDirector().getName())
                .param("lastName", film2.getDirector().getLastName()))
                .andExpect(status().isOk());

        //THEN
        assertThat(filmRepository.count()).isEqualTo(1);
    }

    private String asJson(FilmDto film) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(film);
    }
}