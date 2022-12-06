package com.film.service.service;

import com.film.service.document.FilmDocument;
import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import com.film.service.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmDtoService {

    private final FilmRepository filmRepository;

    public List<FilmDto> getAllFilms() {
        return filmRepository.findAll()
                .stream()
                .map(FilmDocument::toDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getFilmsForDirector(DirectorDto directorDto) {
        return filmRepository.findByDirector(directorDto)
                .stream()
                .map(FilmDocument::toDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilmWithId(int cinemaFilmId) {
        return filmRepository.findByCinemaFilmId(cinemaFilmId)
                .toDto();
    }

    public FilmDto addFilm(FilmDto filmDto) {
        return filmRepository.save(toDocument(filmDto))
                .toDto();
    }

    public FilmDto updateFilm(int cinemaFilmId, FilmDto filmDto) {
        var foundFilm = filmRepository.findByCinemaFilmId(cinemaFilmId);
        var newFilmDocument = toDocument(filmDto);
        newFilmDocument.setId(foundFilm.getId());

        return filmRepository.save(newFilmDocument).toDto();
    }

    public void deleteFilm(int cinemaFilmId) {
        filmRepository.deleteByCinemaFilmId(cinemaFilmId);
    }

    public void deleteFilmOfDirector(DirectorDto directorDto) {
        filmRepository.deleteAllByDirector(directorDto);
    }

    private FilmDocument toDocument(FilmDto filmDto) {
        return FilmDocument.builder()
                .name(filmDto.getName())
                .cinemaFilmId(filmDto.getCinemaFilmId())
                .duration(filmDto.getDuration())
                .genre(filmDto.getGenre())
                .year(filmDto.getYear())
                .description(filmDto.getDescription())
                .director(filmDto.getDirector())
                .build();
    }
}
