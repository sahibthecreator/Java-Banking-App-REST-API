package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

//    @Test
//    void getAllShouldReturnAccountsUpToDefaultLimit() throws Exception {
//        // Mock the behavior of the accountService to return a list of accounts
//        List<Account> mockAccounts = createMockAccounts(); // Create a list of mock accounts
//        when(accountService.getAllAccounts()).thenReturn(mockAccounts);
//
//        this.mockMvc.perform(get("/accounts"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(mockAccounts.size())))
//                .andExpect(jsonPath("$[0].iban").value(mockAccounts.get(0).getIban()));
//    }

//    private List<Account> createMockAccounts() {
//        List<Account> accounts = new ArrayList<>();
//
//        // Create and add mock accounts to the list
//        Account account1 = new Account();
//        account1.setIban("IBAN1");
//        account1.setBalance(1000);
//        account1.setTypeOfAccount(AccountType.SAVINGS);
//        account1.setUser(new User("John Smith"));
//        account1.setActive(true);
//        account1.setDateOfOpening(LocalDate.now());
//        accounts.add(account1);
//
//        Account account2 = new Account();
//        account2.setIban("IBAN2");
//        account2.setBalance(2000);
//        account2.setTypeOfAccount(AccountType.CURRENT);
//        account2.setUser(new User("Jane Smith"));
//        account2.setActive(true);
//        account2.setDateOfOpening(LocalDate.now());
//        accounts.add(account2);
//
//        return accounts;
//    }
}