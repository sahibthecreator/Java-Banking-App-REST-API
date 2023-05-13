package com.bank.app.restapi.service;

import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return new ArrayList<Account>(accountRepository.findAll());
    }

    public Account createAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public void deactivateAccount(Account account) {
        account.setActive(false);
        accountRepository.saveAndFlush(account);
    }

    public List<String> getIbanByUsername(String username) {
        return accountRepository.findIbanByUsername(username);
    }

    public Account getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }
    public float getBalanceByIban(String iban){
        return accountRepository.findBalanceByIban(iban);
    }

}
