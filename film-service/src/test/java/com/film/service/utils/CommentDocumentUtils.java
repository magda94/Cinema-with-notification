package com.film.service.utils;

import autofixture.publicinterface.Any;
import com.film.service.document.CommentDocument;

import java.time.Instant;
import java.util.Random;

public class CommentDocumentUtils {

    public static CommentDocument createCommentDocumentWithFilmId(int filmId) {
        return CommentDocument.builder()
                .userLogin(Any.string())
                .cinemaFilmId(filmId)
                .cinemaCommentId(Any.intValue())
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }

    public static CommentDocument createCommentDocumentWithFilmIdAndStars(int filmId, int stars) {
        return CommentDocument.builder()
                .userLogin(Any.string())
                .cinemaFilmId(filmId)
                .cinemaCommentId(Any.intValue())
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(stars)
                .build();
    }

    public static CommentDocument createCommentDocumentWithFilmIdAndDate(int filmId, Instant date) {
        return CommentDocument.builder()
                .userLogin(Any.string())
                .cinemaFilmId(filmId)
                .cinemaCommentId(Any.intValue())
                .createDate(date)
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }

    public static CommentDocument createCommentDocument() {
        return CommentDocument.builder()
                .userLogin(Any.string())
                .cinemaFilmId(Any.intValue())
                .cinemaCommentId(Any.intValue())
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }
}
