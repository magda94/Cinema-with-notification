package com.film.service.utils;

import autofixture.publicinterface.Any;
import com.film.service.document.FilmDocument;
import com.film.service.dto.Genre;

public class FilmDocumentUtils {

    public static FilmDocument createFilmDocument() {
        return FilmDocument.builder()
                .cinemaFilmId(Any.intValue())
                .name(Any.string())
                .year(Any.intValue().toString())
                .duration(Any.longValue())
                .genre(Any.of(Genre.class))
                .director(DirectorDtoUtils.createDirectorDto())
                .description(Any.string())
                .build();
    }
}
