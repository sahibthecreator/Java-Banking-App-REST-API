package com.bank.app.restapi.Controller;

import com.bank.app.restapi.controller.AccountController;
import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.controller.AccountController;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(accountService);
    }

    @Test
    void getAllShouldReturnListOfAccounts(){
        List<AccountDTO> mockAccounts = Arrays.asList(new AccountDTO(), new AccountDTO());//createMockAccounts();

        when(accountService.getAccounts(null, 0, null, null, null, true, null, 10)).thenReturn(mockAccounts);

        ResponseEntity<List<AccountDTO>> response = accountController.getAccounts(null, 0, null, null, null, true, null, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccounts, response.getBody());
        verify(accountService, times(1)).getAccounts(null, 0, null, null, null, true, null, 10);
    }

    @Test
    void createAccountShouldReturnCreatedAccount() {
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        when(accountService.createAccount(accountDTO)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.createAccount(accountDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
        verify(accountService, times(1)).createAccount(accountDTO);
    }

    @Test
    void accountInfoByIbanShouldReturnAccount(){
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        when(accountService.getAccountByIban("NL01INHO0000000001")).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccountInfo("NL01INHO0000000001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
        verify(accountService, times(1)).getAccountByIban("NL01INHO0000000001");
    }

    @Test
    void getAccountBalanceShouldReturnTheCorrectBalance(){
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        when(accountService.getBalanceByIban("NL01INHO0000000001")).thenReturn(accountDTO.getBalance());

        ResponseEntity<?> response = accountController.getAccountBalance("NL01INHO0000000001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO.getBalance(), response.getBody());
        verify(accountService, times(1)).getBalanceByIban("NL01INHO0000000001");
    }
    // Create a list of 101 accounts
//    private List<AccountDTO> createMockAccounts() {
//        List<AccountDTO> mockAccounts = new ArrayList<>();
//
//        for (int i = 0; i < 99; i++) {
//            AccountDTO mockAccount = AccountDTO.builder()
//                    .iban(accountService.generateDutchIban())
//                    .balance(1000.0f)
//                    .typeOfAccount(AccountType.CURRENT)
//                    .userId(UUID.randomUUID())
//                    .dateOfOpening(LocalDate.now())
//                    .active(true)
//                    .build();
//            mockAccounts.add(mockAccount);
//        }
//
//        return mockAccounts;
//    }
}