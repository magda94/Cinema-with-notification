package com.cinema.service.film.client;

import com.cinema.service.film.configuration.FilmErrorDecoder;
import com.cinema.service.film.dto.FilmDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "filmClient", url = "${com.cinema.service.film}", path = "/films",
configuration = {FilmErrorDecoder.class})
public interface FilmServiceClient {

    @PostMapping("")
    FilmDto addNewFilm(@RequestBody FilmDto filmDto);
}
