package com.film.service.repository;

import com.film.service.document.FilmDocument;
import com.film.service.dto.DirectorDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends MongoRepository<FilmDocument, String> {

    List<FilmDocument> findByDirector(DirectorDto director);

    FilmDocument findByCinemaFilmId(int cinemaFilmId);

    void deleteByCinemaFilmId(int cinemaFilmId);

    void deleteAllByDirector(DirectorDto director);
}
