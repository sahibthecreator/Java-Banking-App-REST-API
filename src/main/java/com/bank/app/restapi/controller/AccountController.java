package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.service.AccountService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

   

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<AccountDTO> accounts = accountService.getAllAccounts().stream().map(accountMapper::toDTO).toList();
            return ResponseEntity.status(200).body(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        // TODO: fix userId field, remove extra "active" field
        // TODO: Check if userId from request body is actually exists
        // TODO: Initialize Iban automatically
        try {
            Account account = accountMapper.toEntity(accountDTO);
            Account newAccount = accountService.createAccount(account);
            AccountDTO newAccountDTO = accountMapper.toDTO(newAccount);
            return ResponseEntity.status(201).body(newAccountDTO);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(400).body("Invalid request body" + e.getMessage()); // Return 400 for bad
                // request
            } else {
                return ResponseEntity.status(500).body("An error occurred: " + e.getMessage()); // Return 500 Internal
                // Server Error
            }
        }
    }

    @GetMapping("/{iban}")
    public ResponseEntity<?> accountInfo(@PathVariable String iban) {
        try {
            // Check if account with iban exists, if not return 404
            Account account = accountService.getAccountByIban(iban);
            AccountDTO accountDTO = accountMapper.toDTO(account);
            return ResponseEntity.status(200).body(accountDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{iban}/balance")
    public ResponseEntity<?> accountBalance(@PathVariable String iban) {
        try {
            // Check if account with iban exists, if not return 404
            double balance = accountService.getBalanceByIban(iban);
            return ResponseEntity.status(200).body(balance);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PatchMapping("/{iban}")
    public ResponseEntity<?> deactivateAccount(@PathVariable String iban) {
        try {
            Account account = accountService.getAccountByIban(iban);
            if (account == null) {
                return ResponseEntity.status(404).body("Account with following Iban not found");
            }

            accountService.deactivateAccount(account);
            return ResponseEntity.status(200).body("Account deactivated");
        } catch (Exception e) {
            return ResponseEntity
                    .status(500).body(e.getMessage());
        }
    }

    // GET account IBAN by full name 
    // /accounts/iban/{fullname}

}
