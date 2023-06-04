package com.bank.app.restapi.cucumber;

import com.bank.app.restapi.controller.AuthController;
import com.bank.app.restapi.controller.UserController;
import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;

import io.cucumber.java.en.*;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private HttpHeaders headers;

    private ResponseEntity<List<UserDTO>> getAllResponse;
    private ResponseEntity<UserDTO> createUserResponse;

    private ObjectMapper objectMapper;

    public UserControllerStepDefinitions() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Given("When the endpoint {string} is available for method {string}")
    public void whenTheEndpointIsAvailableForMethod(String endpoint, String method) {
        LoginDTO loginDTO = new LoginDTO("root@gmail.com", "11111");
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity("/auth/login", loginDTO, LoginResponseDTO.class);

        String jwtToken = loginResponse.getBody().getJwtToken();

        headers.setBearerAuth(jwtToken);

        ResponseEntity<String> response = restTemplate.exchange("/" + endpoint, HttpMethod.OPTIONS,
                new HttpEntity<>(null, headers), String.class);

        List<String> options = Arrays.stream(response.getHeaders()
                .get("Allow")
                .get(0)
                .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I retrieve all users")
    public void iRetrieveAllUsers() throws Exception {
        System.out.println(headers);
        getAllResponse = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<List<UserDTO>>() {
                });
    }

    @Then("the response should have status code {int}")
    public void thenResponseShouldHaveStatusCode(int expectedStatusCode) {
        Assertions.assertTrue(getAllResponse.getStatusCode().value() == expectedStatusCode);
    }

    @And("the response should contain {int} users")
    public void thenResponseShouldContainUsers(int number) {
        int actual = JsonPath.read(getAllResponse.getBody(), "$.size()");
        Assertions.assertTrue(number == actual);
    }


}
