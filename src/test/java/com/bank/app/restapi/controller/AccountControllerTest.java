package com.bank.app.restapi.Controller;

import com.bank.app.restapi.controller.AccountController;
import com.bank.app.restapi.dto.AccountBalanceDTO;
import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.CustomerIbanDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.service.AccountService;

import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountMapper accountMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(accountService);
    }

    @Test
    void getAllShouldReturnListOfAccounts() {
        List<AccountDTO> mockAccounts = Arrays.asList(new AccountDTO(), new AccountDTO());// createMockAccounts();

          when(accountService.getAccounts(null, null, null, null, null, true, null, 10)).thenReturn(mockAccounts);

        ResponseEntity<List<AccountDTO>> response = accountController.getAccounts(null, null, null, null, null, true,
                null, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccounts, response.getBody());
        verify(accountService, times(1)).getAccounts(null, null, null, null, null, true, null, 10);
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
    void accountInfoByIbanShouldReturnAccount() {
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        when(accountService.getAccountDTOByIban("NL01INHO0000000001")).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccountInfo("NL01INHO0000000001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
        verify(accountService, times(1)).getAccountDTOByIban("NL01INHO0000000001");
    }

    @Test
    void getAccountBalanceShouldReturnTheCorrectBalance() {
        AccountBalanceDTO accountDTO = AccountBalanceDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .build();

        when(accountService.getBalanceByIban("NL01INHO0000000001")).thenReturn(accountDTO);

        ResponseEntity<?> response = accountController.getAccountBalance("NL01INHO0000000001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO.getBalance(), response.getBody());
        verify(accountService, times(1)).getBalanceByIban("NL01INHO0000000001");
    }

    @Test
    void getIbanByCustmerNameShouldReturnListOfIbans() {
        List<String> ibans = accountRepository.findIbanByFirstNameAndLastName("Root", "Admin");
        CustomerIbanDTO customerIbanDTO = CustomerIbanDTO.builder()
                .firstName("Root")
                .lastName("Admin")
                .ibanList(ibans)
                .build();

        when(accountService.getIbanByUsername("Root", "Admin")).thenReturn(customerIbanDTO);

        ResponseEntity<CustomerIbanDTO> response = accountController.getIbanByCustomerName("Root", "Admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerIbanDTO, response.getBody());
        verify(accountService, times(1)).getIbanByUsername("Root", "Admin");
    }

    @Test
    void getAccountsByUserIdShouldReturnListOfAccounts() {
        List<AccountDTO> mockAccounts = Arrays.asList(new AccountDTO(), new AccountDTO());
        UUID userId = UUID.randomUUID();

        when(accountService.getAccountsByUserId(userId)).thenReturn(mockAccounts);

        ResponseEntity<List<AccountDTO>> response = accountController.getAccountsByUserId(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccounts, response.getBody());
        verify(accountService, times(1)).getAccountsByUserId(userId);
    }

//    TODO: fix this test
//    @Test
//    void deactivateAccountShouldChangeAccountStatusToNotActive() {
//
//        AccountDTO accountDTO = AccountDTO.builder()
//                .iban("NL01INHO0000000001")
//                .balance(1000.0f)
//                .typeOfAccount(AccountType.CURRENT)
//                .userId(UUID.randomUUID())
//                .dateOfOpening(LocalDate.now())
//                .active(false)
//                .build();
//
//        when(accountService.updateAccountStatus(accountDTO, false, accountDTO.getIban())).thenReturn(true);
//
//        ResponseEntity<String> response = accountController.deactivateAccount(accountDTO.getIban());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Account with iban: " + accountDTO.getIban() + " deactivated", response.getBody());
//        verify(accountService, times(1)).deactivateAccount(accountDTO.getIban());
//
//    }
}