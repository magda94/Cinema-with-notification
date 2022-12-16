package com.cinema.service.film.configuration;

import com.cinema.service.exceptions.FilmConnectionException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FilmErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new FilmConnectionException(getBodyMessage(response), HttpStatus.valueOf(response.status()));
    }

    private String getBodyMessage(Response response) {
        try {
            return new BufferedReader(response.body().asReader(StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "Error during connection";
        }
    }
}
