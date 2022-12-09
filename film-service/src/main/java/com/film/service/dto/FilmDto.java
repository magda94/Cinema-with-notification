package com.film.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {

    private int cinemaFilmId;

    private String name;

    private long duration;

    private Genre genre;

    private String year;

    private String description;

    private DirectorDto director;
}
