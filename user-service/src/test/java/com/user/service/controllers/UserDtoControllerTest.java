package com.user.service.controllers;

import autofixture.publicinterface.Any;
import com.user.service.container.PostgresqlContainer;
import com.user.service.dtos.UserDto;
import com.user.service.repository.UserEntityRepository;
import com.user.service.services.UserDtoService;
import com.user.service.utils.UserDtoUtils;
import com.user.service.utils.UserEntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class UserDtoControllerTest extends PostgresqlContainer {

    @Autowired
    private UserDtoService userDtoService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserDtoController userDtoController;

    @BeforeEach
    public void before() {
        userEntityRepository.deleteAll();
        userDtoController = new UserDtoController(userDtoService);
    }

    @Test
    public void shouldReturnEmptyListWhenNoUserExists() throws Exception {
        //WHEN
        var result = mockMvc.perform( MockMvcRequestBuilders
                        .get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        //GIVEN
        var user1 = UserEntityUtils.createUserEntity();
        var user2 = UserEntityUtils.createUserEntity();

        userEntityRepository.saveAll(List.of(user1, user2));

        //WHEN-THEN
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", equalTo(user1.getLogin())))
                .andExpect(jsonPath("$[1].login", equalTo(user2.getLogin())));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        //GIVEN
        var user = UserDtoUtils.createUserDto();

        //WHEN
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login", equalTo(user.getLogin())));

        //THEN
        var userEntity = userEntityRepository.findByLogin(user.getLogin());
        assertThat(userEntity).isNotEmpty();
        assertThat(userEntity.get().toDto()).isEqualTo(user);
    }

    @Test
    public void shouldNotCreateNewUserIfExistInDb() throws Exception {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDtoWithLogin(userEntity.getLogin());

        //WHEN
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        //THEN
        var size = userEntityRepository.count();
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDtoWithLogin(userEntity.getLogin());

        //WHEN
        mockMvc.perform(put("/users/{login}", userEntity.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", equalTo(userEntity.getLogin())))
                .andExpect(jsonPath("$.name", equalTo(user.getName())))
                .andExpect(jsonPath("$.lastName", equalTo(user.getLastName())));

        //THEN
        var result = userEntityRepository.findByLogin(user.getLogin());
        assertThat(result).isNotEmpty();
        assertThat(result.get().toDto()).isEqualTo(user);
    }

    @Test
    public void shouldNotUpdateIfNotExist() throws Exception {
        //GIVEN
        var user = UserDtoUtils.createUserDto();

        //WHEN
        mockMvc.perform(put("/users/{login}", user.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //THEN
        assertThat(userEntityRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldNotUpdateIfNewLoginExist() throws Exception {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDto();
        userEntityRepository.save(UserEntityUtils.createUserEntityFromDto(user));

        //WHEN
        mockMvc.perform(put("/users/{login}", userEntity.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        //THEN
        var result = userEntityRepository.findByLogin(userEntity.getLogin());
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        //WHEN
        mockMvc.perform(delete("/users/{login}", userEntity.getLogin())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        var result = userEntityRepository.findByLogin(userEntity.getLogin());
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldNotDeleteUserIfNotExist() throws Exception {
        //WHEN-THEN
        mockMvc.perform(delete("/users/{login}", Any.string())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String asJson(UserDto user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }

}