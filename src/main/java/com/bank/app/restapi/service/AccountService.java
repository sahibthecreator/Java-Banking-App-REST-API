package com.bank.app.restapi.service;

import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
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
        account.setIban(generateDutchIban());
        account.setBalance(0);
        account.setDateOfOpening(LocalDate.now());
        account.setActive(true);
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

    public String generateDutchIban() {

        // generate a random 10-digit account number
        String accountNumber = String.valueOf((int) (Math.random() * 1000000000));

        // construct the country code and check digit placeholder
        String countryCode = "NL";
        String checkDigitPlaceholder = "00";

        // concatenate the strings to form the incomplete IBAN
        String incompleteIban = countryCode + checkDigitPlaceholder + accountNumber;

        // calculate the actual check digit using the MOD-97 algorithm
        String checkDigit = calculateCheckDigit(incompleteIban);

        //concatenate the strings to form the complete IBAN

        return countryCode + checkDigit + accountNumber;
    }

    private String calculateCheckDigit(String iban) {
        // move the four initial characters to the end of the string
        String moved = iban.substring(4) + iban.substring(0, 4);

        // convert the letters to numbers (A = 10, B = 11, ..., Z = 35)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moved.length(); i++) {
            char c = moved.charAt(i);
            if (Character.isLetter(c)) {
                sb.append(Character.getNumericValue(c));
            } else {
                sb.append(c);
            }
        }

        // convert the string to a BigInteger
        java.math.BigInteger bigInt = new java.math.BigInteger(sb.toString());

        // calculate the remainder of bigInt / 97
        java.math.BigInteger remainder = bigInt.mod(java.math.BigInteger.valueOf(97));

        // subtract the remainder from 98
        BigInteger checkDigit = BigInteger.valueOf(98).subtract(remainder);

        // pad the check digit with a leading zero if necessary
        return String.format("%02d", checkDigit);
    }
}
