package com.film.service.utils;

import autofixture.publicinterface.Any;
import com.film.service.dto.FilmDto;
import com.film.service.dto.Genre;

public class FilmDtoUtils {

    public static FilmDto createFilmDtoWithId(int id) {
        return FilmDto.builder()
                .cinemaFilmId(id)
                .name(Any.string())
                .year(Any.intValue().toString())
                .duration(Any.longValue())
                .genre(Any.of(Genre.class))
                .director(DirectorDtoUtils.createDirectorDto())
                .description(Any.string())
                .build();
    }

    public static FilmDto createFilmDto() {
        return createFilmDtoWithId(Any.intValue());
    }
}
