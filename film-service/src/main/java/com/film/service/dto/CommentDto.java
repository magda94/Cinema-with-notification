package com.film.service.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
public class CommentDto {

    @NotBlank (message = "User login is mandatory")
    private String userLogin;

    @NotNull(message = "Film id is mandatory")
    private int cinemaFilmId;

    @NotNull(message = "Comment id is mandatory")
    private int cinemaCommentId;

    @NotNull(message = "Creation date is mandatory")
    private Instant createDate;

    private String comment;

    private int stars;
}
