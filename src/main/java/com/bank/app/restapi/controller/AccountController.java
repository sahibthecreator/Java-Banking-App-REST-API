package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.service.AccountService;
import com.bank.app.restapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    private final AccountMapper accountMapper;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<AccountDTO> allAccounts = accountService.getAllAccounts()
                .stream()
                .map(accountMapper::toDTO)
                .limit(1000)
                .collect(Collectors.toList());

        if (allAccounts.isEmpty()) {
            return ResponseEntity.status(404).body("No accounts found");
        }

        return ResponseEntity.status(200).body(allAccounts);
    }

    // TODO: 24-May-23 test returns 500 internal server error
    @PostMapping("")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountDTO accountDTO) {

        AccountDTO createdAccountDTO = accountService.createAccount(accountDTO);

        return ResponseEntity.status(201).body(createdAccountDTO);
    }

    @GetMapping("/{iban}")
    public ResponseEntity<?> accountInfo(@PathVariable String iban) {
        AccountDTO accountDTO = accountService.getAccountByIban(iban);

        if (accountDTO == null) {
            return ResponseEntity.status(404).body("Account with following Iban not found");
        }

        return ResponseEntity.status(200).body(accountDTO);
    }

    // TODO: 24-May-23 test returns 200 but it's the response for the get accountInfo
    // or we make a BalanceResponseDTO and return that
    @GetMapping("/{iban}/balance")
    public ResponseEntity<?> accountBalance(@PathVariable String iban) {
        double balance = accountService.getBalanceByIban(iban);

        if (balance == -1) {
            return ResponseEntity.status(404).body("Account with following Iban not found");
        }

        return ResponseEntity.status(200).body(balance);
    }

    // TODO: 2021-10-13 test returns 200 but I don't think it works
    @PatchMapping("/{iban}")
    public ResponseEntity<?> deactivateAccount(@PathVariable String iban) {
        AccountDTO accountDTO = accountService.getAccountByIban(iban);

        if (accountDTO == null) {
            return ResponseEntity.status(404).body("Account with following Iban "+iban+" is not found");
        }

        accountService.deactivateAccount(accountDTO);
        return ResponseEntity.status(200).body("Account deactivated");
    }


    @GetMapping("/{userName}/iban")
    public ResponseEntity<?> getIbanByUsername(@PathVariable String userName) {
        List<String> ibans = accountService.getIbanByUsername(userName);

        if (ibans.isEmpty()) {
            return ResponseEntity.status(204).body("No IBANs found for this username");
        }

        return ResponseEntity.status(200).body(ibans);
    }

}
