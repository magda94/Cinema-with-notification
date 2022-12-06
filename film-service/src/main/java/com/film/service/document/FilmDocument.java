package com.film.service.document;

import com.film.service.dto.Genre;
import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("films")
@Builder
@Data
public class FilmDocument {

    @Id
    private String id;

    private int cinemaFilmId;

    private String name;

    private long duration;

    private Genre genre;

    private String year;

    private String description;

    private DirectorDto director;

    public FilmDto toDto() {
        return FilmDto.builder()
                .name(this.name)
                .cinemaFilmId(this.cinemaFilmId)
                .duration(this.duration)
                .genre(this.genre)
                .year(this.year)
                .description(this.description)
                .director(this.director)
                .build();
    }
}
