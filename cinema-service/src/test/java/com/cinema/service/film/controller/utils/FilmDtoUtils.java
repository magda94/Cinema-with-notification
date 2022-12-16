package com.cinema.service.film.controller.utils;

import autofixture.publicinterface.Any;
import com.cinema.service.film.dto.DirectorDto;
import com.cinema.service.film.dto.FilmDto;
import com.cinema.service.film.dto.Genre;

public class FilmDtoUtils {

    public static FilmDto createAnyFilmDto() {
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

    public static FilmDto createFilmDtoWithId(int filmId) {
        return FilmDto.builder()
                .cinemaFilmId(filmId)
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
