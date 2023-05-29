package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.service.AccountService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateOfOpening,
            @RequestParam(required = false) Boolean active,
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

    @GetMapping("/{iban}")
    public ResponseEntity<AccountDTO> getAccountInfo(@PathVariable String iban) {
        return ResponseEntity.status(200).body(accountService.getAccountByIban(iban));
    }

    // TODO: 27-May-23 test returns 200 but return whole account object
    // should we make a BalanceResponseDTO?
    @GetMapping("/{iban}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable String iban) {
        return ResponseEntity.status(200).body(accountService.getBalanceByIban(iban));
    }

    @GetMapping("/{customerName}")
    public ResponseEntity<List<String>> getIbanByCustomerName(@PathVariable String customerName) {
        return ResponseEntity.status(200).body(accountService.getIbanByUsername(customerName));
    }


    @PatchMapping("/{iban}")
    public ResponseEntity<?> deactivateAccount(@PathVariable String iban) {
        return ResponseEntity.status(200).body(accountService.deactivateAccount(iban));
    }

    // TODO: test returns 200 but doesn't change the status (i tried both patch and
    // post requests)
    // @PostMapping("/{iban}")
    // public ResponseEntity<?> activateAccount(@PathVariable String iban) {
    // accountService.activateAccount(iban);
    // return ResponseEntity.status(200).build();
    // }

}