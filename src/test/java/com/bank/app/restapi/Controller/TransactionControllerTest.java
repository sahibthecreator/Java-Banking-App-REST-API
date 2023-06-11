 package com.bank.app.restapi.controller;

 import com.bank.app.restapi.config.UserData;
 import com.bank.app.restapi.dto.TransactionDTO;
 import com.bank.app.restapi.model.TransactionType;
 import com.bank.app.restapi.service.TransactionService;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.core.Authentication;

 import java.time.LocalDate;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.UUID;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.mockito.Mockito.*;

 class TransactionControllerTest {
     @Mock
     private TransactionService transactionService;

     private TransactionController transactionController;

     @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this);
         transactionController = new TransactionController(transactionService);
     }

     @Test
     void testGetTransactions() {
         List<TransactionDTO> transactions = new ArrayList<>();
         when(transactionService.getTransactions(
                 anyString(), anyFloat(), anyFloat(), anyFloat(), any(TransactionType.class),
                 any(LocalDate.class), any(LocalDate.class)
         )).thenReturn(transactions);

         ResponseEntity<List<TransactionDTO>> response = transactionController.getTransactions(
                 null, null, null, null, null, null, null);

         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
         assertEquals(transactions, response.getBody());
     }

     @Test
     void testGetTransactionById() {
         UUID transactionId = UUID.randomUUID();
         TransactionDTO transaction = new TransactionDTO();
         when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

         ResponseEntity<TransactionDTO> response = transactionController.getTransactionById(transactionId);

         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertEquals(transaction, response.getBody());
     }

     @Test
     void testGetTransactionsByUserId() {
         UUID userId = UUID.randomUUID();
         List<TransactionDTO> transactions = new ArrayList<>();

         Authentication authentication = mock(Authentication.class);
         UserData userData = mock(UserData.class);
         when(userData.getId()).thenReturn(userId);
         when(authentication.getPrincipal()).thenReturn(userData);

         when(transactionService.getTransactionsByUserId(
                 eq(userId), anyString(), anyFloat(), anyFloat(), anyFloat(), any(TransactionType.class),
                 any(LocalDate.class), any(LocalDate.class), any(UUID.class)
         )).thenReturn(transactions);

         ResponseEntity<List<TransactionDTO>> response = transactionController.getTransactionsByUserId(
                 userId, null, null, null, null, null, null, null, authentication);

         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
         assertEquals(transactions, response.getBody());
     }

     @Test
     void testAddTransaction() {
         TransactionDTO transactionDTO = new TransactionDTO();
         TransactionType typeOfTransaction = TransactionType.TRANSFER;
         UUID performingUserId = UUID.randomUUID();

         Authentication authentication = mock(Authentication.class);
         UserData userData = mock(UserData.class);
         when(userData.getId()).thenReturn(performingUserId);
         when(authentication.getPrincipal()).thenReturn(userData);

         TransactionDTO savedTransactionDTO = new TransactionDTO();
         when(transactionService.addTransaction(transactionDTO, typeOfTransaction, performingUserId))
                 .thenReturn(savedTransactionDTO);

         ResponseEntity<TransactionDTO> response = transactionController.addTransaction(null, transactionDTO, authentication);

         assertEquals(HttpStatus.CREATED, response.getStatusCode());
         assertEquals(savedTransactionDTO, response.getBody());
     }
 }
