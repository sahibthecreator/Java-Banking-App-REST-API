package com.bank.app.restapi.cucumber;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.AccountType;
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
    private ResponseEntity<List<AccountDTO>> accountsResponse;
    private ResponseEntity<AccountDTO> accountResponse;

    private ResponseEntity response;
    private AccountDTO accountDTO = new AccountDTO();
    HttpHeaders headers;

    private UUID accountId = UUID.randomUUID();

    private ObjectMapper objectMapper;

    public AccountControllerStepDefinitions() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        objectMapper.registerModule(new JavaTimeModule());
    }
    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        LoginDTO loginDTO = new LoginDTO("root@gmail.com", "11111");
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity("/auth/login", loginDTO, LoginResponseDTO.class);

        String jwtToken = Objects.requireNonNull(loginResponse.getBody()).getJwtToken();

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


    // get all accounts
    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() {
        ParameterizedTypeReference<List<AccountDTO>> responseType = new ParameterizedTypeReference<List<AccountDTO>>() {};
        accountsResponse = restTemplate.exchange("/accounts", HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

    }

    @Then("The response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, accountsResponse.getStatusCodeValue());
    }

    @And("The response should be a list of accounts")
    public void theResponseShouldBeAListOfAccounts() {
        Assertions.assertNotNull(accountsResponse.getBody());
    }


    // create account
    @And("I have a request with account details")
    public void iHaveARequestWithAccountDetails(String requestJson) throws JsonProcessingException {
        accountDTO = objectMapper.readValue(requestJson, AccountDTO.class);

    }

    @When("I send a POST request to the {string} endpoint with the account details")
    public void iSendAPOSTRequestToTheEndpointWithTheAccountDetails(String endpoint) {
        accountResponse = restTemplate.postForEntity(endpoint, new HttpEntity<>(accountDTO, headers), AccountDTO.class);
    }

    @Then("A new account should be created")
    public void aNewAccountShouldBeCreated() {
        Assertions.assertEquals(201, accountResponse.getStatusCode().value());
        AccountDTO newAccount = accountResponse.getBody();
        Assertions.assertNotNull(accountDTO);
        Assertions.assertNotNull(accountDTO.getUserId());
        accountId = newAccount.getId();

    }

    // retrieve account info

    @When("I retrieve the account info for IBAN {string}")
    public void iRetrieveTheAccountInfoForIBAN(String iban) {
        accountResponse = restTemplate.exchange("/accounts/" + iban, HttpMethod.GET, new HttpEntity<>(null, headers), AccountDTO.class);
    }

    @And("The response body should contain the account details")
    public void theResponseBodyShouldContainTheAccountDetails() {
        Assertions.assertNotNull(accountResponse.getBody());
    }



    //retrieve ibans by customer name

    @When("I retrieve IBANs by customer name with the following details:")
    public void iRetrieveIBANsByCustomerNameWithTheFollowingDetails() {
        ParameterizedTypeReference<List<AccountDTO>> responseType = new ParameterizedTypeReference<List<AccountDTO>>() {};
        accountsResponse = restTemplate.exchange("/accounts/iban?firstname=John%20%lastname=Doe", HttpMethod.GET, new HttpEntity<>(null, headers), responseType);
    }
    @And("The response body should contain a list of CustomerIbanDTOs")
    public void theResponseBodyShouldContainAListOfCustomerIbanDTOs() {
        Assertions.assertNotNull(accountsResponse.getBody());
    }


    // deactivate account
    @When("I deactivate the account with IBAN {string}")
    public void iDeactivateTheAccountWithIBAN(String iban) {
        response = restTemplate.patchForObject("/accounts/" + iban, null, ResponseEntity.class);
    }

    @Then("The response status fro deactivating account should be {int}")
    public void theResponseStatusFroDeactivatingAccountShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCodeValue());
    }
    @And("The response body should contain a success message")
    public void theResponseBodyShouldContainASuccessMessage() {
        Assertions.assertNotNull(response.getBody());
    }

    
}
