package com.film.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExtendCommentDto {

    private int cinemaFilmId;

    private String filmName;

    private List<CommentDto> comments;
}
