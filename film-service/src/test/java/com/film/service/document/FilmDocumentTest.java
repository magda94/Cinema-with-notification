package com.film.service.document;


import autofixture.publicinterface.Any;
import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import com.film.service.dto.Genre;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FilmDocumentTest {

    @Test
    public void shouldCreateDto() {

        var name = Any.string();
        var director = DirectorDto.builder()
                .name(Any.string())
                .lastName(Any.string())
                .build();
        var description = Any.string();
        var year = Any.intValue();
        var duration = Any.longValue();
        var genre = Any.of(Genre.class);

        FilmDocument filmDocument = FilmDocument.builder()
                .id(UUID.randomUUID().toString())
                .cinemaFilmId(1)
                .name(name)
                .director(director)
                .year(year.toString())
                .duration(duration)
                .genre(genre)
                .description(description)
                .build();

        FilmDto filmDto = FilmDto.builder()
                .cinemaFilmId(1)
                .name(name)
                .director(director)
                .year(year.toString())
                .duration(duration)
                .genre(genre)
                .description(description)
                .build();

        assertThat(filmDocument.toDto()).isEqualTo(filmDto);
    }
}