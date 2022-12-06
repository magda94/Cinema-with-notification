package com.film.service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FilmDto {

    private int cinemaFilmId;

    private String name;

    private long duration;

    private Genre genre;

    private String year;

    private String description;

    private DirectorDto director;
}
