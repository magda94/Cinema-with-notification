package com.cinema.service.film.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.WireMockConfig;
import com.cinema.service.container.PostgresqlContainer;
import com.cinema.service.film.dto.DirectorDto;
import com.cinema.service.film.dto.FilmDto;
import com.cinema.service.film.dto.Genre;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@ContextConfiguration(classes = { WireMockConfig.class })
class FilmControllerTest extends PostgresqlContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer mockServer;

    @Test
    public void shouldAddFilm() throws Exception {
        //GIVEN
        var film = createAnyFilmDto();

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/films"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(asJson(film)))
        );


        //WHEN-THEN
        mockMvc.perform(post("/films")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(film.getName())));
    }

    @Test
    public void shouldNotAddFilm() throws Exception {
        //GIVEN
        var film = createAnyFilmDto();

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/films"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.CONFLICT.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("Error"))
        );

        //WHEN-THEN
        mockMvc.perform(post("/films")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(film)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", equalTo("Error")));
    }

    private String asJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private FilmDto createAnyFilmDto() {
        return FilmDto.builder()
                .cinemaFilmId(Any.intValue())
                .name(Any.string())
                .year(Any.intValue().toString())
                .description(Any.string())
                .duration(Any.longValue())
                .genre(Any.of(Genre.class))
                .director(DirectorDto.builder()
                        .name(Any.string())
                        .lastName(Any.string())
                        .build())
                .build();
    }
}