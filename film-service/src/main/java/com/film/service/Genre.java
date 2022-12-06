package com.film.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Genre {

    COMEDY("comedy"),
    DRAMA("drama"),
    THRILLER("thriller"),
    SCIFI("sci-fi");

    @Getter
    private final String name;
}
