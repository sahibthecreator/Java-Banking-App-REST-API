package com.bank.app.restapi.controller;


import com.bank.app.restapi.controller.TransactionController;
import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    public TransactionControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testGetTransactions() throws Exception {
        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO());
        transactions.add(new TransactionDTO());

        when(transactionService.getTransactions(anyString(), anyFloat(), anyFloat(), anyFloat(),
                any(TransactionType.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .param("iban", "NL34INHO516485984")
                        .param("startDate", "01-01-2022")
                        .param("endDate", "31-12-2022")
                        .param("minAmount", "100.0")
                        .param("maxAmount", "1000.0")
                        .param("exactAmount", "500.0")
                        .param("typeOfTransaction", "TRANSFER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService).getTransactions(eq("NL34INHO516485984"), eq(100.0f), eq(1000.0f), eq(500.0f),
                eq(TransactionType.TRANSFER), eq(LocalDate.parse("2022-01-01")), eq(LocalDate.parse("2022-12-31")));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        UUID transactionId = UUID.randomUUID();
        TransactionDTO transaction = new TransactionDTO();
        transaction.setId(transactionId);

        when(transactionService.getTransactionById(any(UUID.class))).thenReturn(transaction);

        mockMvc.perform(get("/transactions/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionId.toString()));

        verify(transactionService).getTransactionById(eq(transactionId));
    }

    @Test
    public void testGetTransactionsByUserId() throws Exception {

        UUID userId = UUID.randomUUID();
        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO());
        transactions.add(new TransactionDTO());

        when(transactionService.getTransactionsByUserId(any(UUID.class))).thenReturn(transactions);

        mockMvc.perform(get("/transactions/userId/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService).getTransactionsByUserId(eq(userId));
    }

    @Test
    public void testAddTransaction() throws Exception {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setAmount(500.0f);

        when(transactionService.addTransaction(any(TransactionDTO.class), any(TransactionType.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 500.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500.0));

        verify(transactionService).addTransaction(eq(transaction), eq(TransactionType.TRANSFER));
    }
}
