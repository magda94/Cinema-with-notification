package com.film.service.repository;

import com.film.service.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<CommentDocument, String> {

    boolean existsByCinemaCommentId(int cinemaCommentId);

    Optional<CommentDocument> findByCinemaCommentId(int cinemaCommentId);

    List<CommentDocument> findAllByCinemaFilmId(int cinemaFilmId);

    void deleteByCinemaCommentId(int cinemaCommentId);

    void deleteAllByCinemaFilmId(int cinemaFilmId);
}
