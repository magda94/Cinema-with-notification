package com.film.service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DirectorDto {

    private String name;

    private String lastName;
}
