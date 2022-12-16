package com.cinema.service.film.controller;

import com.cinema.service.film.client.FilmServiceClient;
import com.cinema.service.film.dto.FilmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmServiceClient filmServiceClient;

    @PostMapping("")
    public ResponseEntity addFilm(@RequestBody FilmDto filmDto) {
        log.info("POST");
        return ResponseEntity.ok(filmServiceClient.addNewFilm(filmDto));
    }
}
