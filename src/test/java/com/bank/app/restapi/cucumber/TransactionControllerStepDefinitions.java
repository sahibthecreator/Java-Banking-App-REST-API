 package com.bank.app.restapi.cucumber;

 import com.bank.app.restapi.config.UserData;
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
 import org.springframework.security.core.Authentication;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.UUID;

 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.when;

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
         Mockito.when(transactionService.getTransactions(iban, null, null, null, null, null, null))
                 .thenReturn(transactions);

         multipleTransactionsResponse = transactionController.getTransactions(iban, null, null, null, null, null, null);
     }

     @Then("I should receive a response with status {int} because there is no transactions")
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

         Authentication authentication = mock(Authentication.class);
         UserData userData = mock(UserData.class);
         when(userData.getId()).thenReturn(uuid);
         when(authentication.getPrincipal()).thenReturn(userData);

         List<TransactionDTO> transactions = new ArrayList<>();
         Mockito.when(transactionService.getTransactionsByUserId(uuid, null, null, null, null, null, null, null, uuid)).thenReturn(transactions);

         multipleTransactionsResponse = transactionController.getTransactionsByUserId(uuid, null, null, null, null, null, null, null, authentication);
     }

     @Then("I should receive a response with status {int} because there is no transactions by userId")
     public void checkGetTransactionsByUserIdResponse(int expectedStatus) {
         HttpStatus status = (HttpStatus) multipleTransactionsResponse.getStatusCode();
         Assertions.assertEquals(expectedStatus, status.value());

         List<TransactionDTO> transactions = multipleTransactionsResponse.getBody();
         Assertions.assertNotNull(transactions);
     }


     @When("I call the addTransaction endpoint with transaction type {string}")
     public void callAddTransactionEndpoint(String transactionType) {

         UUID performingUserId = UUID.randomUUID();
         Authentication authentication = mock(Authentication.class);
         UserData userData = mock(UserData.class);
         when(userData.getId()).thenReturn(performingUserId);
         when(authentication.getPrincipal()).thenReturn(userData);

         TransactionDTO transactionDTO = new TransactionDTO();
         TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());

         Mockito.when(transactionService.addTransaction(transactionDTO, type, performingUserId)).thenReturn(transactionDTO);

         singleTransactionResponse = transactionController.addTransaction(null, transactionDTO, authentication);
     }

     @Then("I should receive a response with status {int} and the added transaction")
     public void checkAddTransactionResponse(int expectedStatus) {
         HttpStatus status = (HttpStatus) singleTransactionResponse.getStatusCode();
         Assertions.assertEquals(expectedStatus, status.value());

         TransactionDTO transaction = singleTransactionResponse.getBody();
         Assertions.assertNotNull(transaction);
     }

 }
