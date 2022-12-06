package com.film.service.controllers;

import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import com.film.service.service.FilmDtoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmDtoController {

    private final FilmDtoService filmDtoService;

    @GetMapping("")
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        log.info("Get all films request");
        return ResponseEntity.ok(filmDtoService.getAllFilms());
    }

    @GetMapping("/director")
    public ResponseEntity<List<FilmDto>> getFilmsForDirector(DirectorDto director) {
        log.info("Get all films for director: {} request", director.toString());
        return ResponseEntity.ok(filmDtoService.getFilmsForDirector(director));
    }

    @GetMapping("/{cinemaFilmId}")
    public ResponseEntity<FilmDto> getFilmWithCinemaFilmId(@PathVariable("cinemaFilmId") Integer cinemaFilmId) {
        log.info("Get film with cinemaFilmId: {} request", cinemaFilmId);
        return ResponseEntity.ok(filmDtoService.getFilmWithId(cinemaFilmId));
    }

    @PostMapping("")
    public ResponseEntity<FilmDto> addFilm(@RequestBody FilmDto filmDto) {
        log.info("Create film with cinemaFilmId: {} request", filmDto.getCinemaFilmId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(filmDtoService.addFilm(filmDto));
    }

    @PutMapping("/{cinemaFilmId}")
    public ResponseEntity<FilmDto> updateFilm(@PathVariable("cinemaFilmId") Integer cinemaFilmId, @RequestBody FilmDto filmDto) {
        log.info("Update film with cinemaFilmId: {} request", cinemaFilmId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(filmDtoService.updateFilm(cinemaFilmId, filmDto));
    }

    @DeleteMapping("/{cinemaFilmId}")
    public ResponseEntity deleteFilmWithId(@PathVariable("cinemaFilmId") Integer cinemaFilmId) {
        log.info("Delete film with cinemaFilmId: {} request", cinemaFilmId);
        filmDtoService.deleteFilm(cinemaFilmId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/director")
    public ResponseEntity deleteAllFilmsForDirector(DirectorDto director) {
        log.info("Delete all films for director: {} request", director.toString());
        filmDtoService.deleteFilmOfDirector(director);
        return ResponseEntity.ok().build();
    }
}
