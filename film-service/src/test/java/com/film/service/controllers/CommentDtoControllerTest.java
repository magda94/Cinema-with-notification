package com.film.service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.film.service.container.MongoDbContainer;
import com.film.service.dto.CommentDto;
import com.film.service.repository.CommentRepository;
import com.film.service.repository.FilmRepository;
import com.film.service.utils.CommentDocumentUtils;
import com.film.service.utils.FilmDocumentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
class CommentDtoControllerTest extends MongoDbContainer {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        filmRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllComments() throws Exception {
        //GIVEN
        var film1 = FilmDocumentUtils.createFilmDocument();
        var film2 = FilmDocumentUtils.createFilmDocument();

        filmRepository.saveAll(List.of(film1, film2));

        var comment1 = CommentDocumentUtils.createCommentDocumentWithFilmId(film1.getCinemaFilmId());
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmId(film2.getCinemaFilmId());
        var comment3 = CommentDocumentUtils.createCommentDocumentWithFilmId(film1.getCinemaFilmId());

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN-THEN
        mockMvc.perform(get("/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].filmName", equalTo(film1.getName())))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].filmName", equalTo(film2.getName())))
                .andExpect(jsonPath("$[1].comments", hasSize(1)));
    }

    @Test
    public void shouldReturnCommentsReversedStarsOrder() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();

        filmRepository.save(film);

        var comment1 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 3);
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 4);
        var comment3 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 1);

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN-THEN
        mockMvc.perform(get(String.format("/comments/%d/starsSorted", film.getCinemaFilmId()))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("reversed", Boolean.TRUE.toString()))
                .andDo(print())
                .andExpect(jsonPath("$.filmName", equalTo(film.getName())))
                .andExpect(jsonPath("$.comments", hasSize(3)))
                .andExpect(jsonPath("$.comments[0].stars", equalTo(4)))
                .andExpect(jsonPath("$.comments[2].stars", equalTo(1)));

    }

    @Test
    public void shouldReturnCommentsNotReversedStarsOrder() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();

        filmRepository.save(film);

        var comment1 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 3);
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 4);
        var comment3 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndStars(film.getCinemaFilmId(), 1);

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN-THEN
        mockMvc.perform(get(String.format("/comments/%d/starsSorted", film.getCinemaFilmId()))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("reversed", Boolean.FALSE.toString()))
                .andDo(print())
                .andExpect(jsonPath("$.filmName", equalTo(film.getName())))
                .andExpect(jsonPath("$.comments", hasSize(3)))
                .andExpect(jsonPath("$.comments[0].stars", equalTo(1)))
                .andExpect(jsonPath("$.comments[2].stars", equalTo(4)));

    }

    @Test
    public void shouldReturnCommentsReversedDateOrder() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();

        filmRepository.save(film);

        var timestamp = Instant.now();

        var comment1 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp);
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp.plus(1000L, ChronoUnit.DAYS));
        var comment3 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp.minus(1000L, ChronoUnit.DAYS));

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN-THEN
        mockMvc.perform(get(String.format("/comments/%d/dateSorted", film.getCinemaFilmId()))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("reversed", Boolean.TRUE.toString()))
                .andDo(print())
                .andExpect(jsonPath("$.filmName", equalTo(film.getName())))
                .andExpect(jsonPath("$.comments", hasSize(3)))
                .andExpect(jsonPath("$.comments[0].cinemaCommentId", equalTo(comment2.getCinemaCommentId())))
                .andExpect(jsonPath("$.comments[2].cinemaCommentId", equalTo(comment3.getCinemaCommentId())));

    }

    @Test
    public void shouldReturnCommentsNotReversedDateOrder() throws Exception {
        //GIVEN
        var film = FilmDocumentUtils.createFilmDocument();

        filmRepository.save(film);

        var timestamp = Instant.now();

        var comment1 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp);
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp.plus(1000L, ChronoUnit.DAYS));
        var comment3 = CommentDocumentUtils.createCommentDocumentWithFilmIdAndDate(film.getCinemaFilmId(), timestamp.minus(1000L, ChronoUnit.DAYS));

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN-THEN
        mockMvc.perform(get(String.format("/comments/%d/dateSorted", film.getCinemaFilmId()))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("reversed", Boolean.FALSE.toString()))
                .andDo(print())
                .andExpect(jsonPath("$.filmName", equalTo(film.getName())))
                .andExpect(jsonPath("$.comments", hasSize(3)))
                .andExpect(jsonPath("$.comments[0].cinemaCommentId", equalTo(comment3.getCinemaCommentId())))
                .andExpect(jsonPath("$.comments[2].cinemaCommentId", equalTo(comment2.getCinemaCommentId())));

    }

//    @Test
//    public void shouldAddComment() throws Exception {
//        //GIVEN
//        var film = FilmDocumentUtils.createFilmDocument();
//        filmRepository.save(film);
//
//        var comment = CommentDtoUtils.createCommentDtoForFilmId(film.getCinemaFilmId());
//
//        //WHEN
//        mockMvc.perform(post("/comments")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJson(comment)))
//                .andExpect(status().isOk());
//
//        //THEN
//        var result = commentRepository.findByCinemaCommentId(comment.getCinemaCommentId());
//        assertThat(result.isPresent()).isTrue();
//        assertThat(result.get().toDto()).isEqualTo(comment);
//    }

    @Test
    public void shouldDeleteComment() throws Exception {
        //GIVEN
        var comment = CommentDocumentUtils.createCommentDocument();
        commentRepository.save(comment);

        //WHEN
        mockMvc.perform(delete("/comments/" + comment.getCinemaCommentId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(commentRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteCommentsForFilm() throws Exception {
        //GIVEN
        var comment1 = CommentDocumentUtils.createCommentDocument();
        var comment2 = CommentDocumentUtils.createCommentDocumentWithFilmId(comment1.getCinemaFilmId());
        var comment3 = CommentDocumentUtils.createCommentDocument();

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        //WHEN
        mockMvc.perform(delete("/comments/film/" + comment1.getCinemaFilmId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        assertThat(commentRepository.count()).isEqualTo(1);
    }

    private String asJson(CommentDto commentDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(commentDto);
    }
}