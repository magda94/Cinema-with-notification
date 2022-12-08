package com.film.service.utils;

import autofixture.publicinterface.Any;
import com.film.service.dto.CommentDto;

import java.time.Instant;
import java.util.Random;

public class CommentDtoUtils {

    public static CommentDto createCommentDtoForFilmId(int filmId) {
        return CommentDto.builder()
                .userLogin(Any.string())
                .cinemaFilmId(filmId)
                .cinemaCommentId(Any.intValue())
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }

    public static CommentDto createCommentDtoWithId(int commentId) {
        return CommentDto.builder()
                .userLogin(Any.string())
                .cinemaFilmId(Any.intValue())
                .cinemaCommentId(commentId)
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }

    public static CommentDto createCommentDto() {
        return CommentDto.builder()
                .userLogin(Any.string())
                .cinemaFilmId(Any.intValue())
                .cinemaCommentId(Any.intValue())
                .createDate(Instant.now())
                .comment(Any.string())
                .stars(new Random().nextInt(0, 6))
                .build();
    }
}
