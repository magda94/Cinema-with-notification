package com.user.service.controllers;

import com.user.service.container.PostgresqlContainer;
import com.user.service.repository.UserEntityRepository;
import com.user.service.services.UserDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
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

}