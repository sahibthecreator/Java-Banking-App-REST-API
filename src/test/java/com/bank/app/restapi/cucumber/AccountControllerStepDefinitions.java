package com.bank.app.restapi.cucumber;

import com.bank.app.restapi.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class AccountControllerStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<List<AccountDTO>> allAccountsResponse;
    private ResponseEntity<AccountDTO> createdAccountResponse;
    private ResponseEntity<AccountDTO> retrievedAccountResponse;
    private ResponseEntity<List<CustomerIbanDTO>> customerIbansResponse;
    String ibanToRetrieve;
    private AccountDTO createdAccountDTO;
    private AccountDTO retrievedAccountDTO ;
    HttpHeaders headers;

    private final ObjectMapper objectMapper;
    private final AuthControllerStepDefinitions authControllerStepDefinitions;

    public AccountControllerStepDefinitions(AuthControllerStepDefinitions authControllerStepDefinitions) {
        this.authControllerStepDefinitions = authControllerStepDefinitions;
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private void loggedInUser() {
        LoginDTO loginDTO = new LoginDTO("root@gmail.com", "11111");
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity("/auth/login", loginDTO, LoginResponseDTO.class);

        String jwtToken = Objects.requireNonNull(loginResponse.getBody()).getJwtToken();

        headers.set("Authorization", "Bearer " + jwtToken);

    }

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {

        loggedInUser();

        ResponseEntity<String> response = restTemplate.exchange("/" + endpoint, HttpMethod.OPTIONS,
                new HttpEntity<>(null, headers), String.class);

        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)
                        .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }


    // get all accounts
    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() {
        System.out.println(headers);
        allAccountsResponse = restTemplate.exchange("/accounts",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<List<AccountDTO>>() {});


    }

    @Then("The response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, allAccountsResponse.getStatusCode().value());
    }

    @And("The response should be a list of accounts")
    public void theResponseShouldBeAListOfAccounts() {
        Assertions.assertNotNull(allAccountsResponse.getBody());
    }


    // create account

    @When("I send a POST request to the {string} endpoint with the account details")
    public void iSendAPOSTRequestToTheEndpointWithTheAccountDetails(String endpoint, String requestJson) throws JsonProcessingException {
        System.out.println(requestJson);
        System.out.println(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        createdAccountDTO = objectMapper.readValue(requestJson, AccountDTO.class);
        System.out.println(createdAccountDTO);
        createdAccountResponse = restTemplate.postForEntity(
                "/" + endpoint,
                new HttpEntity<>(createdAccountDTO, headers),
                AccountDTO.class);

        createdAccountDTO = createdAccountResponse.getBody();
        assert createdAccountDTO != null;
        ibanToRetrieve = createdAccountResponse.getBody().getIban();
    }

    @Then("A new account should be created")
    public void aNewAccountShouldBeCreated() {
        Assertions.assertNotNull(createdAccountResponse.getBody());
        Assertions.assertEquals(createdAccountDTO, createdAccountResponse.getBody());
    }




}
