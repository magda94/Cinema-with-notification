package com.film.service.document;

import autofixture.publicinterface.Any;
import com.film.service.dto.CommentDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDocumentTest {

    @Test
    public void shouldCreateDto() {

        var userLogin = Any.string();
        var comment = Any.string();
        var stars = Any.intValue();
        var createDate = Instant.now();

        CommentDocument commentDocument = CommentDocument.builder()
                .id(UUID.randomUUID().toString())
                .cinemaFilmId(1)
                .cinemaCommentId(2)
                .userLogin(userLogin)
                .createDate(createDate)
                .comment(comment)
                .stars(stars)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .cinemaCommentId(2)
                .cinemaFilmId(1)
                .userLogin(userLogin)
                .createDate(createDate)
                .comment(comment)
                .stars(stars)
                .build();

        assertThat(commentDocument.toDto()).isEqualTo(commentDto);
    }

}