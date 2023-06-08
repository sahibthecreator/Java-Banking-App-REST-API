package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountBalanceDTO;
import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.AccountRequestDTO;
import com.bank.app.restapi.dto.CustomerIbanDTO;
import com.bank.app.restapi.service.AccountService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(#userId, authentication)")
    public ResponseEntity<List<AccountDTO>> getAccounts(
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) Float balance,
            @RequestParam(required = false) String typeOfAccount,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) LocalDate dateOfOpening,
            @RequestParam(required = false) boolean active,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "20") int limit) {

        List<AccountDTO> accounts = accountService.getAccounts(iban, balance, typeOfAccount, userId, dateOfOpening,
                active, sort, limit);
        return ResponseEntity.status(200).body(accounts);
    }

    @PostMapping("")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        return ResponseEntity.status(201).body(accountService.createAccount(accountDTO));
    }

    //Why not accounts/{iban}?
    @GetMapping("/{iban}/accountInfo")
    @PreAuthorize("@securityExpressions.loggedIn(authentication)")
    public ResponseEntity<AccountDTO> getAccountInfo(@PathVariable String iban) {
        AccountDTO accountDTO = accountService.getAccountDTOByIban(iban);
        return ResponseEntity.status(200).body(accountDTO);
    }

    @GetMapping("/{iban}/balance")
    @PreAuthorize("@securityExpressions.loggedIn(authentication)")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance(@PathVariable String iban) {
        AccountBalanceDTO accountBalanceDTO = accountService.getBalanceByIban(iban);
        return ResponseEntity.status(200).body(accountBalanceDTO);
    }

    //Retrieves all users if no parameter values are put?
    //Why not 204 response when empty?
    //Why not /accounts/findCustomers?
    @GetMapping("/iban")
    @PreAuthorize("@securityExpressions.loggedIn(authentication)")
    public ResponseEntity<List<CustomerIbanDTO>> getIbanByCustomerName(@RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname) {
        List<CustomerIbanDTO> customerIbanDTOs = accountService.getIbanByUsername(firstname, lastname);
        return ResponseEntity.status(200).body(customerIbanDTOs);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(#userId, authentication)")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable UUID userId) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.status(200).body(accounts);
    }

    @PutMapping("/{iban}")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<String> updateAccountAbsoluteLimit(@PathVariable String iban, @RequestBody @Valid AccountDTO accountDTO) {
        accountService.updateAbsoluteLimit(iban, accountDTO);
        return ResponseEntity.status(200).build();
    }

    //Why not accounts/{iban}/deactivate ?
    @PatchMapping("/{iban}")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<String> deactivateAccount(@PathVariable String iban) {
        String response = accountService.deactivateAccount(iban);
        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{iban}/activate")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<String> activateAccount(@PathVariable String iban) {
        String response = accountService.activateAccount(iban);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/requests")
    @PreAuthorize("@securityExpressions.loggedIn(authentication)")
    public ResponseEntity<AccountRequestDTO> submitAccountRequest(@RequestBody @Valid AccountRequestDTO requestDto) {
        AccountRequestDTO accountRequestDTO = accountService.submitAccountRequest(requestDto);
        return ResponseEntity.status(200).body(accountRequestDTO);
    }

    @GetMapping("/requests")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<List<AccountRequestDTO>> getAllRequests() {
        List<AccountRequestDTO> requests = accountService.getAllRequests();
        return ResponseEntity.status(200).body(requests);
    }

    @PutMapping("/requests/{requestId}/approve")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<AccountDTO> approveBankAccountRequest(@PathVariable UUID requestId) {
        AccountDTO createdAccountDTO = accountService.approveAccountRequest(requestId);
        return ResponseEntity.status(200).body(createdAccountDTO);
    }

    @PutMapping("/requests/{requestId}/deny")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<AccountRequestDTO> denyBankAccountRequest(@PathVariable UUID requestId) {
        accountService.denyBankAccountRequest(requestId);
        return ResponseEntity.status(200).build();
    }

}