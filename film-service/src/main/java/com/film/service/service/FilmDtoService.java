package com.film.service.service;

import com.film.service.document.FilmDocument;
import com.film.service.dto.DirectorDto;
import com.film.service.dto.FilmDto;
import com.film.service.exceptions.FilmNotFoundException;
import com.film.service.exceptions.FilmWithIdExistException;
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
                .map(FilmDocument::toDto)
                .orElseThrow(() -> new FilmNotFoundException("Cannot find film with cinemaFilmId: " + cinemaFilmId));
    }

    public FilmDto addFilm(FilmDto filmDto) {
        filmRepository.findByCinemaFilmId(filmDto.getCinemaFilmId())
                .ifPresent(s -> {
                    throw new FilmWithIdExistException(String.format("Film with cinemaFilmId : %d exists in database", filmDto.getCinemaFilmId()));
                });

        return filmRepository.save(toDocument(filmDto))
                .toDto();
    }

    public FilmDto updateFilm(int cinemaFilmId, FilmDto filmDto) {
        var foundFilm = filmRepository.findByCinemaFilmId(cinemaFilmId);

        if (foundFilm.isEmpty()) {
            throw new FilmNotFoundException("Cannot find film with cinemaFilmId: " + cinemaFilmId);
        }

        if(filmRepository.existsByCinemaFilmId(filmDto.getCinemaFilmId()) &&
                cinemaFilmId != filmDto.getCinemaFilmId()) {
            throw new FilmWithIdExistException(String.format("Film with cinemaFilmId: %d exists in database", filmDto.getCinemaFilmId()));
        }

        var newFilmDocument = toDocument(filmDto);
        newFilmDocument.setId(foundFilm.get().getId());

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
