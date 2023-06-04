package com.bank.app.restapi.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.UserType;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private ResponseEntity<LoginResponseDTO> loginResponse;
    private ResponseEntity<UserDTO> registerResponse;

    private ObjectMapper objectMapper;


    public AuthControllerStepDefinitions() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    // Step definitions

    @Given("I have login request with email {string} and password {string}")
    public void iHaveLoginRequestWithEmailAndPassword(String email, String password) {
        LoginDTO loginDTO = new LoginDTO(email, password);
        String url = "http://localhost:8080" + port + "/auth/login";
        loginResponse = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
    }

    @When("I send login request")
    public void iSendLoginRequest() {
        // The request is already sent in the previous step
    }

    @Then("I receive login response with status code {int}")
    public void iReceiveLoginResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), loginResponse.getStatusCode());
    }

    @Then("I receive valid login response")
    public void iReceiveValidLoginResponse() {
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().getJwtToken());
        assertNotNull(loginResponse.getBody().getUserId());
    }

    @Given("I have registration request with details")
    public void iHaveRegistrationRequestWithDetails(String requestJson) throws IOException {
        RegisterDTO registerDTO = objectMapper.readValue(requestJson, RegisterDTO.class);

        String url = "http://localhost:" + port + "/auth/register";
        registerResponse = restTemplate.postForEntity(url, registerDTO, UserDTO.class);
    }

    @When("I send registration request")
    public void iSendRegistrationRequest() {
        // The request is already sent in the previous step
    }

    @Then("I receive registration response with status code {int}")
    public void iReceiveRegistrationResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), registerResponse.getStatusCode());
    }

    @Then("I receive valid registration response")
    public void iReceiveValidRegistrationResponse() {
        System.out.println(registerResponse);
        assertNotNull(registerResponse.getBody());
        //assertNotNull(registerResponse.getBody().getId());
        assertNotNull(registerResponse.getBody().getRole());
    }
}