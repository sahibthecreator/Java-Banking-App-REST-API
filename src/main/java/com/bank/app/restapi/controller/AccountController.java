package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.service.AccountService;
import com.bank.app.restapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    private final AccountMapper accountMapper;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            List<AccountDTO> accounts = accountService.getAllAccounts().stream().map(accountMapper::toDTO).toList();
            if (accounts.isEmpty()) {
                return ResponseEntity.status(404).body("No accounts found");
            }

            return ResponseEntity.status(200).body(accounts);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        try {
            // check if request body is valid
            if (!accountMapper.isValidDTO(accountDTO)) {
                return ResponseEntity.status(400).body("Invalid request body"); // Return 400 for bad request
            }

            // check if user exists
            if (!userService.userIdExists(accountDTO.getUserId())) {
                return ResponseEntity.status(404).body("User with following id not found");
            }

            AccountDTO createdAccountDTO = accountService.createAccount(accountDTO);
            return ResponseEntity.status(201).body(createdAccountDTO);

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
            Account account = accountService.getAccountByIban(iban);

            if (account == null) {
                return ResponseEntity.status(404).body("Account with following Iban not found");
            }

            AccountDTO accountDTO = accountMapper.toDTO(account);
            return ResponseEntity.status(200).body(accountDTO);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{iban}/balance")
    public ResponseEntity<?> accountBalance(@PathVariable String iban) {
        try {
            double balance = accountService.getBalanceByIban(iban);

            if (balance == -1) {
                return ResponseEntity.status(404).body("Account with following Iban not found");
            }

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

    // TODO: 20-May-23 review this method
    // do I need to check if user exists and return 404 if not?
    // or I should return 204 if no accounts found for this username?
    @GetMapping("/{userName}/iban")
    public ResponseEntity<?> getIbanByUsername(@PathVariable String userName) {
        try {
            List<String> ibans = accountService.getIbanByUsername(userName);

            if (ibans.isEmpty()) {
                return ResponseEntity.status(204).body("No IBANs found for this username");
            }

            return ResponseEntity.status(200).body(ibans);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
