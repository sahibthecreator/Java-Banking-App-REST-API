package com.bank.app.restapi.cucumber;

import com.bank.app.restapi.controller.AuthController;
import com.bank.app.restapi.controller.UserController;
import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;

import io.cucumber.java.en.*;

import java.util.*;

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
    private ResponseEntity<UserDTO> getByIdResponse;
    private ResponseEntity<UserDTO> updateUserResponse;
    private ResponseEntity<HashMap<String, String>> deleteUserResponse;

    private UUID createdUserId;


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

        System.out.println("Headers:          " + endpoint);
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
        Assertions.assertEquals(getAllResponse.getStatusCode().value(), expectedStatusCode);
    }

    @And("the response should contain {int} users")
    public void thenResponseShouldContainUsers(int number) {
        int actual = JsonPath.read(getAllResponse.getBody(), "$.size()");
        Assertions.assertEquals(number, actual);
    }


    @When("I request a creation of user with details")
    public void iCreateNewUser(String requestJson) throws JsonProcessingException {
        RegisterDTO registerDTO = objectMapper.readValue(requestJson, RegisterDTO.class);


        createUserResponse = restTemplate.postForEntity(
                "/users",
                new HttpEntity<>(registerDTO, headers),
                UserDTO.class);
    }

    @Then("the user should be created successfully")
    public void thenUserShouldBeCreatedSuccessfully() {
        Assertions.assertEquals(201, createUserResponse.getStatusCode().value());
        UserDTO createdUser = createUserResponse.getBody();
        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        createdUserId = createdUser.getId();
    }
    @Given("When the endpoint {string} is available for method {string} with userId")
    public void whenTheEndpointIsAvailableForMethodWithUserId(String endpoint, String method) {
        System.out.println(createdUserId);
        String url = "/users/" + createdUserId; // how i can use endpoint here dynamically
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.OPTIONS,
                new HttpEntity<>(null, headers), String.class);

        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)
                        .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I retrieve the user by ID")
    public void iRetrieveUserById() {
        String url = "/users/" + createdUserId;
        getByIdResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                UserDTO.class);
    }

    @Then("the user with ID should exist")
    public void thenUserWithIdShouldExist() {
        Assertions.assertEquals(200, getByIdResponse.getStatusCodeValue());
        UserDTO retrievedUser = getByIdResponse.getBody();
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(createdUserId, retrievedUser.getId());
    }

    @When("I update the user details")
    public void iUpdateUserDetails(String requestJson) throws JsonProcessingException {
        UserDTO updateUserDTO = objectMapper.readValue(requestJson, UserDTO.class);

        String url = "/users/" + createdUserId;
        updateUserResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(updateUserDTO, headers),
                UserDTO.class);
    }

    @Then("the user details should be updated")
    public void thenUserDetailsShouldBeUpdated() {
        Assertions.assertEquals(200, updateUserResponse.getStatusCode().value());
        UserDTO updatedUser = updateUserResponse.getBody();
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(createdUserId, updatedUser.getId());
    }

    @When("I delete the user")
    public void iDeleteUser() {
        String url = "/users/" + createdUserId;
        deleteUserResponse = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<HashMap<String, String>>() {
                });
    }

    @Then("the user should be deleted")
    public void thenUserShouldBeDeleted() {
        Assertions.assertEquals(200, deleteUserResponse.getStatusCode().value());
        HashMap<String, String> responseMap = deleteUserResponse.getBody();
        Assertions.assertNotNull(responseMap);
        Assertions.assertEquals("User "+ createdUserId +" has been permanently deleted", responseMap.get("message"));
    }
}
