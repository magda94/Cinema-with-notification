package com.cinema.service.user.controller;

import autofixture.publicinterface.Any;
import com.cinema.service.user.dto.UserRequest;
import com.cinema.service.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

//    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

//    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    public void before() {
//        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        //GIVEN
        var userRequest = createAnyUserRequest();

//        mockRestServiceServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://localhost:8080/users")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.CREATED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(asJson(userRequest))
//                );

        //WHEN
        var result = mockMvc.perform(post("/users/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(userRequest)))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var exceptedResponse = UserResponse.builder()
                .login(userRequest.getLogin())
                .build();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(asJson(exceptedResponse));
    }

    @Test
    public void shouldNotRegisterUser() throws Exception {
        //GIVEN
        var userRequest = createAnyUserRequest();

//        mockRestServiceServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://localhost:8080/users")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.CONFLICT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(asJson(new RuntimeException()))
//                );

        //WHEN-THEN
        mockMvc.perform(post("/users/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(userRequest)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private UserRequest createAnyUserRequest() {
        return UserRequest.builder()
                .login(Any.string())
                .name(Any.string())
                .lastName(Any.string())
                .build();
    }

    private String asJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}