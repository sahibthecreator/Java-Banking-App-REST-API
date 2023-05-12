package com.bank.app.restapi.controller;

import com.bank.app.restapi.http.HttpBody;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service){
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity all_accounts(){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getAllAccounts()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("get_iban_by_username/{userName}")
    public ResponseEntity get_iban_by_username(@PathVariable String userName){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getIBANByUsername(userName)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("get_accountInfo/{iban}")
    public ResponseEntity get_accountInfo(@PathVariable String iban){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getAccountByIBAN(iban)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("get_accountBalance/{iban}")
    public ResponseEntity get_accountBalance(@PathVariable String iban){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getBalanceByIBAN(iban)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("create_account")
    public ResponseEntity create_account(@RequestBody Account account){
        try{
            return ResponseEntity.status(201).body(
                    new HttpBody<>(true, service.createAccount(account)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PatchMapping("deactivate_account/{iban}")
    public ResponseEntity deactivate_account(@PathVariable String iban, @RequestBody Account account){
        try{
            Account existingAccount = service.getAccountByIBAN(iban);
            if(existingAccount == null){
                return ResponseEntity.status(404).body(
                        new HttpBody<>(false, "Account not found"));
            }
            service.deactivateAccount(existingAccount);
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, "Account deactivated"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
