package com.bank.app.restapi.controller;

import com.bank.app.restapi.controller.AccountController;
import com.bank.app.restapi.dto.*;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.service.AccountService;

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
import java.util.Optional;
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

        assertEquals(accountDTO, response.getBody());
        verify(accountService, times(1)).getBalanceByIban("NL01INHO0000000001");
    }

    @Test
    void getIbanByCustomerNameShouldReturnListOfIbans() {
        List<CustomerIbanDTO> ibans = accountRepository.findIbanByFirstNameAndLastName("Root", "Admin");

        when(accountService.getIbansByUsername("Root", "Admin")).thenReturn(ibans);

        ResponseEntity<List<CustomerIbanDTO>> response = accountController.getIbansByCustomerName("Root", "Admin");
        // assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ibans, response.getBody());
        verify(accountService, times(1)).getIbansByUsername("Root", "Admin");
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
    
    @Test
    void deactivateAccountShouldChangeAccountStatusToNotActive() {
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        AccountStatusDTO accountStatusDTO = AccountStatusDTO.builder()
                .active(false)
                .build();
        when(accountService.deactivateAccount(accountDTO.getIban())).thenReturn(accountStatusDTO);

        ResponseEntity<AccountStatusDTO> response = accountController.deactivateAccount(accountDTO.getIban());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountStatusDTO, response.getBody());

        verify(accountService, times(1)).deactivateAccount(accountDTO.getIban());
    }

    @Test
    void activateAccountShouldChangeAccountStatusToActive() {
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .dateOfOpening(LocalDate.now())
                .active(false)
                .build();

        AccountStatusDTO accountStatusDTO = AccountStatusDTO.builder()
                .active(false)
                .build();
        when(accountService.activateAccount(accountDTO.getIban())).thenReturn(accountStatusDTO);

        ResponseEntity<AccountStatusDTO> response = accountController.activateAccount(accountDTO.getIban());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountStatusDTO, response.getBody());

        verify(accountService, times(1)).activateAccount(accountDTO.getIban());
    }

    //TODO: if status becomes enum, change this test
    @Test
    void submitAccountRequestShouldReturnCreatedAccountRequest() {
        AccountRequestDTO accountRequestDTO = AccountRequestDTO.builder()
                .accountType(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .fullName("Root Admin")
                .status("PENDING")
                .build();

        when(accountService.submitAccountRequest(accountRequestDTO)).thenReturn(accountRequestDTO);

        ResponseEntity<AccountRequestDTO> response = accountController.submitAccountRequest(accountRequestDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accountRequestDTO, response.getBody());
        verify(accountService, times(1)).submitAccountRequest(accountRequestDTO);
    }

    @Test
    void getAccountRequestsShouldReturnListOfAccountRequests() {
        List<AccountRequestDTO> mockAccountRequests = Arrays.asList(new AccountRequestDTO(), new AccountRequestDTO());

        when(accountService.getAllRequests()).thenReturn(mockAccountRequests);

        ResponseEntity<List<AccountRequestDTO>> response = accountController.getAllRequests();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountRequests, response.getBody());
        verify(accountService, times(1)).getAllRequests();
    }

    @Test
    void approveAccountRequestShouldReturnApprovedAccountRequest() {
        AccountDTO accountDTO = AccountDTO.builder()
                .iban("NL01INHO0000000001")
                .balance(1000.0f)
                .typeOfAccount(AccountType.CURRENT)
                .userId(UUID.fromString("a03a7dd6-5fc8-4b95-b575-9f286f35088d"))
                .dateOfOpening(LocalDate.now())
                .active(true)
                .build();

        when(accountService.approveAccountRequest(accountDTO.getId())).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.approveBankAccountRequest(accountDTO.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
        verify(accountService, times(1)).approveAccountRequest(accountDTO.getId());
    }

    @Test
    void denyAccountRequestShouldReturnNoContent() {
        UUID requestId = UUID.randomUUID();
        AccountRequestDTO accountRequestDTO = AccountRequestDTO.builder()
                .accountType(AccountType.CURRENT)
                .userId(UUID.randomUUID())
                .fullName("Root Admin")
                .status("PENDING")
                .build();

        when(accountService.denyBankAccountRequest(requestId)).thenReturn(accountRequestDTO);

        ResponseEntity<AccountRequestDTO> response = accountController.denyBankAccountRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService, times(1)).denyBankAccountRequest(requestId);
    }

}