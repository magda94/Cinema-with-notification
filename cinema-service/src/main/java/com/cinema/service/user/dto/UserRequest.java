package com.cinema.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull
    private String login;

    @NotNull
    private String name;

    @NotNull
    private String lastName;
}
