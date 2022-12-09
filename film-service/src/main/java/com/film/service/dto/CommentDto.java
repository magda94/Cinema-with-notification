package com.film.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
