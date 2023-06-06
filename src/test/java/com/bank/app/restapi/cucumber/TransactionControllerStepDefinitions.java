package com.bank.app.restapi.cucumber;

import com.bank.app.restapi.controller.TransactionController;
import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.service.TransactionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class TransactionControllerStepDefinitions {

    private TransactionController transactionController;
    private TransactionService transactionService;
    private ResponseEntity<TransactionDTO> singleTransactionResponse;
    private ResponseEntity<List<TransactionDTO>> multipleTransactionsResponse;

    @Given("a TransactionController")
    public void createTransactionController() {
        transactionService = Mockito.mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);
    }

    @When("I call the getTransactions endpoint with parameters")
    public void callGetTransactionsEndpoint() {
        String iban = "NL16INHO76255591";

        List<TransactionDTO> transactions = new ArrayList<>();
        Mockito.when(transactionService.getTransactions(iban, null, null, null, null, null, null, null))
                .thenReturn(transactions);

        multipleTransactionsResponse = transactionController.getTransactions(iban, null, null, null, null, null, null, null);
    }

    @Then("I should receive a response with status {int} and a list of transactions")
    public void checkGetTransactionsResponse(int expectedStatus) {
        HttpStatus status = (HttpStatus) multipleTransactionsResponse.getStatusCode();
        Assertions.assertEquals(expectedStatus, status.value());

        List<TransactionDTO> transactions = multipleTransactionsResponse.getBody();
        Assertions.assertNotNull(transactions);
    }

    @When("I call the getTransactionById endpoint with transactionId {string}")
    public void callGetTransactionByIdEndpoint(String transactionId) {
        UUID uuid = UUID.fromString(transactionId);

        TransactionDTO transactionDTO = new TransactionDTO();
        Mockito.when(transactionService.getTransactionById(uuid)).thenReturn(transactionDTO);

        singleTransactionResponse = transactionController.getTransactionById(uuid);
    }

    @Then("I should receive a response with status {int} and a single transaction")
    public void checkGetTransactionByIdResponse(int expectedStatus) {
        HttpStatus status = (HttpStatus) singleTransactionResponse.getStatusCode();
        Assertions.assertEquals(expectedStatus, status.value());

        TransactionDTO transaction = singleTransactionResponse.getBody();
        Assertions.assertNotNull(transaction);
    }

    @When("I call the getTransactionsByUserId endpoint with userId {string}")
    public void callGetTransactionsByUserIdEndpoint(String userId) {
        UUID uuid = UUID.fromString(userId);

        List<TransactionDTO> transactions = new ArrayList<>();
        Mockito.when(transactionService.getTransactionsByUserId(uuid)).thenReturn(transactions);

        multipleTransactionsResponse = transactionController.getTransactionsByUserId(uuid);
    }

    @Then("I should receive a response with status {int} and a list of transactions by userId")
    public void checkGetTransactionsByUserIdResponse(int expectedStatus) {
        HttpStatus status = (HttpStatus) multipleTransactionsResponse.getStatusCode();
        Assertions.assertEquals(expectedStatus, status.value());

        List<TransactionDTO> transactions = multipleTransactionsResponse.getBody();
        Assertions.assertNotNull(transactions);
    }


    @When("I call the addTransaction endpoint with transaction type {string}")
    public void callAddTransactionEndpoint(String transactionType) {
        TransactionDTO transactionDTO = new TransactionDTO();
        TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());

        Mockito.when(transactionService.addTransaction(transactionDTO, type, null)).thenReturn(transactionDTO);

        singleTransactionResponse = transactionController.addTransaction(null, transactionDTO, null);
    }

    @Then("I should receive a response with status {int} and the added transaction")
    public void checkAddTransactionResponse(int expectedStatus) {
        HttpStatus status = (HttpStatus) singleTransactionResponse.getStatusCode();
        Assertions.assertEquals(expectedStatus, status.value());

        TransactionDTO transaction = singleTransactionResponse.getBody();
        Assertions.assertNotNull(transaction);
    }

}
