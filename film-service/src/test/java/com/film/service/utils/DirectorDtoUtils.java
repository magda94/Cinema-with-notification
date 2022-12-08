package com.film.service.utils;

import autofixture.publicinterface.Any;
import com.film.service.dto.DirectorDto;

public class DirectorDtoUtils {

    public static DirectorDto createDirectorDto() {
        return DirectorDto.builder()
                .name(Any.string())
                .lastName(Any.string())
                .build();
    }
}
