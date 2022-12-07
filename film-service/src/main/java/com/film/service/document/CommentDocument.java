package com.film.service.document;

import com.film.service.dto.CommentDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("comments")
@Data
@Builder
public class CommentDocument {

    @Id
    private String id;

    private String userLogin;

    private int cinemaFilmId;

    private int cinemaCommentId;

    private Instant createDate;

    private String comment;

    private int stars;

    public CommentDto toDto() {
        return CommentDto.builder()
                .userLogin(this.userLogin)
                .cinemaFilmId(this.cinemaFilmId)
                .cinemaCommentId(this.cinemaCommentId)
                .createDate(this.createDate)
                .comment(this.comment)
                .stars(this.stars)
                .build();
    }
}
