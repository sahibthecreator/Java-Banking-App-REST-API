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
    public ResponseEntity allAccounts(){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getAllAccounts()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/accounts")
    public ResponseEntity createAccount(@RequestBody Account account){
        try{
            return ResponseEntity.status(201).body(
                    new HttpBody<>(true, service.createAccount(account)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/accounts/{iban}/info")
    public ResponseEntity accountInfo(@PathVariable String iban){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getAccountByIban(iban)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/accounts/{iban}/info-balance")
    public ResponseEntity accountBalance(@PathVariable String iban){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getBalanceByIban(iban)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PatchMapping("/accounts/{iban}/deactivate")
    public ResponseEntity deactivateAccount(@PathVariable String iban){
        try{
            Account existingAccount = service.getAccountByIban(iban);
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

//    @GetMapping("/accounts/{userName}/iban")
//    public ResponseEntity ibanByUsername(@PathVariable String userName){
//        try{
//            return ResponseEntity.status(200).body(
//                    new HttpBody<>(true, service.getIbanByUsername(userName)));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }

}
