package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountBalanceDTO;
import com.bank.app.restapi.dto.AccountDTO;
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
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
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

    @GetMapping("/{iban}/accountInfo")
    public ResponseEntity<AccountDTO> getAccountInfo(@PathVariable String iban) {
        AccountDTO accountDTO = accountService.getAccountByIban(iban);
        return ResponseEntity.status(200).body(accountDTO);
    }

    @GetMapping("/{iban}/balance")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance(@PathVariable String iban) {
        AccountBalanceDTO accountBalanceDTO = accountService.getBalanceByIban(iban);
        return ResponseEntity.status(200).body(accountBalanceDTO);
    }

    @GetMapping("/iban")
    public ResponseEntity<CustomerIbanDTO> getIbanByCustomerName(@RequestParam(required=false) String firstname, @RequestParam(required=false) String lastname) {
        CustomerIbanDTO customerIbanDTO = accountService.getIbanByUsername(firstname, lastname);
        return ResponseEntity.status(200).body(customerIbanDTO);
    }

     @GetMapping("/{userId}")
     public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable UUID userId) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(userId);
         return ResponseEntity.status(200).body(accounts);
     }

    @PatchMapping("/{iban}")
    public ResponseEntity<String> deactivateAccount(@PathVariable String iban) {
        String response = accountService.deactivateAccount(iban);
        return ResponseEntity.status(200).body(response);
    }

     @PatchMapping("/{iban}/activate")
     public ResponseEntity<String > activateAccount(@PathVariable String iban) {
     String response = accountService.activateAccount(iban);
     return ResponseEntity.status(200).body(response);
     }

}