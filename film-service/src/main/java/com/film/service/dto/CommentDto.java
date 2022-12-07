package com.film.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentDto {

    private String userLogin;

    private int cinemaFilmId;

    private int cinemaCommentId;

    private Instant createDate;

    private String comment;

    private int stars;
}
