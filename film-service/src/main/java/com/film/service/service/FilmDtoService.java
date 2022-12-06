package com.film.service.service;

import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmDtoService {

    public List<FilmDto> getAllFilms() {
        return List.of();
    }

    public List<FilmDto> getFilmsForDirector(DirectorDto directorDto) {
        return List.of();
    }

    public FilmDto getFilmWithId(int cinemaFilmId) {
        return null;
    }

    public FilmDto addFilm(FilmDto filmDto) {
        return null;
    }

    public FilmDto updateFilm(int cinemaFilmId, FilmDto filmDto) {
        return null;
    }

    public void deleteFilm(int cinemaFilmId) {
    }

    public void deleteFilmOfDirector(DirectorDto directorDto) {

    }
}
