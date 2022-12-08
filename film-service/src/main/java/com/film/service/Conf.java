package com.film.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.ThreeTenBackPortConverters;

@Configuration
public class Conf {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
//        JavaTimeModule
        return new ObjectMapper().findAndRegisterModules();
    }
}
