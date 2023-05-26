package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.service.AccountService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final Environment environment;
    private final AccountMapper accountMapper;

    @GetMapping("")
    public ResponseEntity<?> getAll() {

        int defaultLimit = Integer.parseInt(environment.getProperty("api.account.default-limit", "1000"));
        List<AccountDTO> allAccounts = accountService.getAllAccounts()
                .stream()
                .map(accountMapper::toDTO)
                .limit(defaultLimit)
                .collect(Collectors.toList());

        if (allAccounts.isEmpty()) {
            return ResponseEntity.status(404).body("No accounts found");
        }

        return ResponseEntity.status(200).body(allAccounts);
    }

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

    // TODO: 27-May-23 test returns 200 but return whole account object
    // should we make a BalanceResponseDTO?
    @GetMapping("/{iban}/balance")
    public ResponseEntity<?> accountBalance(@PathVariable String iban) {
        double balance = accountService.getBalanceByIban(iban);

        if (balance == -1) {
            return ResponseEntity.status(404).body("Account with following Iban not found");
        }

        return ResponseEntity.status(200).body(balance);
    }

    @PatchMapping("/{iban}")
    public ResponseEntity<?> deactivateAccount(@PathVariable String iban) {
        accountService.deactivateAccount(iban);
        return ResponseEntity.status(200).build();
    }

    //TODO: test returns 200 but doesn't change the status (i tried both patch and post requests)
//    @PostMapping("/{iban}")
//    public ResponseEntity<?> activateAccount(@PathVariable String iban) {
//        accountService.activateAccount(iban);
//        return ResponseEntity.status(200).build();
//    }


    @GetMapping("/customerIBANs")
    public ResponseEntity<?> getIbanByCustomerName(@PathVariable String customerName) {
        List<String> ibans = accountService.getIbanByUsername(customerName);

        if (ibans.isEmpty()) {
            return ResponseEntity.status(204).body("No IBANs found for this username");
        }

        return ResponseEntity.status(200).body(ibans);
    }

}
