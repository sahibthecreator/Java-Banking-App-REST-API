package com.bank.app.restapi.controller;

import com.bank.app.restapi.http.HttpBody;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> allAccounts(){
        try{
            List<Account> accounts = service.getAllAccounts();
            return ResponseEntity
                    .ok()
                    .body(new HttpBody<>(true, accounts));
        }catch (Exception e){
            return ResponseEntity
                    .status(404)
                    .body(new HttpBody<>(false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity createAccount(@RequestBody Account account){
        try{
            Account newAccount = service.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new HttpBody<>(true, newAccount));
        }catch (Exception e){
            return ResponseEntity
                    .status(400)
                    .body(new HttpBody<>(false, e.getMessage()));
        }
    }

    @GetMapping("/{iban}/info")
    public ResponseEntity accountInfo(@PathVariable String iban){
        try{
            return ResponseEntity.ok()
                    .body(new HttpBody<>(true, service.getAccountByIban(iban)));
        }catch (Exception e){
            return ResponseEntity
                    .status(404)
                    .body(new HttpBody<>(false, e.getMessage()));
        }
    }

    @GetMapping("/{iban}/info-balance")
    public ResponseEntity accountBalance(@PathVariable String iban){
        try{
            return ResponseEntity.ok()
                    .body(new HttpBody<>(true, Collections.singletonMap("balance", service.getBalanceByIban(iban))));
        }catch (Exception e){
            return ResponseEntity
                    .status(404)
                    .body(new HttpBody<>(false, e.getMessage()));
        }
    }

    @PatchMapping("/{iban}/deactivate")
    public ResponseEntity deactivateAccount(@PathVariable String iban){
        try{
            Account existingAccount = service.getAccountByIban(iban);
            if(existingAccount == null){
                return ResponseEntity
                        .ok()
                        .body(new HttpBody<>(false, "Account not found"));
            }
            service.deactivateAccount(existingAccount);
            return ResponseEntity
                    .ok()
                    .body(new HttpBody<>(true, "Account deactivated"));
        }catch (Exception e){
            return ResponseEntity
                    .status(400)
                    .body(new HttpBody<>(false, e.getMessage()));
        }
    }

}
