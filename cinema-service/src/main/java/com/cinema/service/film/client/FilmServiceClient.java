package com.cinema.service.film.client;

import com.cinema.service.film.configuration.FilmErrorDecoder;
import com.cinema.service.film.dto.FilmDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "filmClient", url = "${com.cinema.service.film}", path = "/films",
configuration = {FilmErrorDecoder.class})
public interface FilmServiceClient {

    @GetMapping("/{filmId}")
    FilmDto getFilmWithId(@PathVariable("filmId") int filmId);

    @PostMapping("")
    FilmDto addNewFilm(@RequestBody FilmDto filmDto);
}
