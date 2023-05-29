package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.repository.AccountRepository;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserService userService;

    private final AccountMapper accountMapper;

    public List<AccountDTO> getAccounts(String iban,
            Float balance,
            String typeOfAccount,
            UUID userId,
            LocalDate dateOfOpening,
            Boolean active,
            String sortDirection,
            int limit) {

        Specification<Account> specification = buildSpecification(iban, balance, typeOfAccount, userId, dateOfOpening,
                active);
        Sort sort;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, "dateOfOpening");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "dateOfOpening");
        }
        Page<Account> accounPage = accountRepository.findAll(specification, PageRequest.of(0, limit, sort));
        List<Account> accountList = accounPage.getContent();

        if (accountList.isEmpty()) {
            throw new EntityNotFoundException("No accounts found");
        }

        return accountList.stream().map(accountMapper::toDTO).toList();
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {

        UUID userId = accountDTO.getUserId();
        if (!userService.userIdExists(userId)) {
            throw new EntityNotFoundException("User with following id: " + userId + " not found");
        }

        String dutchIban = generateDutchIban();

        accountDTO.setUserId(userId);
        accountDTO.setIban(dutchIban);
        accountDTO.setBalance(accountDTO.getBalance());
        accountDTO.setDateOfOpening(LocalDate.now());
        accountDTO.setActive(true);

        Account account = accountMapper.toEntity(accountDTO);
        account = accountRepository.saveAndFlush(account);

        return accountMapper.toDTO(account);
    }

    //The BANK's bank account
    public void createBankAccount(UUID id) {
        AccountDTO bank = new AccountDTO();
        bank.setIban("NL01INHO0000000001");
        bank.setBalance(10000);
        bank.setTypeOfAccount(AccountType.CURRENT);
        //the account had to be connected to the root admin, userId filed in AccountDTO is annotated with @NotNull
        bank.setUserId(id);
        bank.setDateOfOpening(LocalDate.now());
        bank.setAbsoluteLimit(0);
        bank.setActive(true);

        Account account = accountMapper.toEntity(bank);
        account = accountRepository.saveAndFlush(account);
    }

    public boolean deactivateAccount(String iban) {
        AccountDTO accountDTO = getAccountByIban(iban);
        return updateAccountStatus(accountDTO, false, iban);
    }

    public boolean activateAccount(String iban) {
        AccountDTO accountDTO = getAccountByIban(iban);
        return updateAccountStatus(accountDTO, true, iban);
    }

    public List<String> getIbanByUsername(String customerName) {
        List<String> ibans = accountRepository.findIbanByUsername(customerName);
        if (ibans.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for user: " + customerName);
        }
        return ibans;
    }

    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for user with id: " + userId);
        }
        return accounts.stream().map(accountMapper::toDTO).toList();
    }

    public AccountDTO getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        return accountMapper.toDTO(account);
    }

    public float getBalanceByIban(String iban) {
        return accountRepository.findBalanceByIban(iban);
    }

    public boolean ibanExists(String dutchIban) {
        Account account = accountRepository.findByIban(dutchIban);
        return account != null;
    }

    public String generateDutchIban() {

        // generate a random 10-digit account number
        String accountNumber = String.valueOf((int) (Math.random() * 1000000000));

        // construct the country code and check digit placeholder
        String countryCode = "NL";
        String checkDigitPlaceholder = "00";
        String customCode = "INHO";

        // concatenate the strings to form the incomplete IBAN
        String incompleteIban = countryCode + checkDigitPlaceholder + accountNumber;

        // calculate the actual check digit using the MOD-97 algorithm
        String checkDigit = calculateCheckDigit(incompleteIban);

        // concatenate the strings to form the complete IBAN

        String iban = countryCode + checkDigit + customCode + accountNumber;

        while (ibanExists(iban)) {
            iban = generateDutchIban();
        }
        return iban;

    }

    // Private methods
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

    private boolean updateAccountStatus(AccountDTO accountDTO, boolean active, String iban) {
        if (accountDTO == null) {
            throw new EntityNotFoundException("No account with the following iban " + iban);
        }
        Account account = accountMapper.toEntity(accountDTO);
        account.setActive(active);
        this.accountRepository.saveAndFlush(account);
        return true;
    }

    private Specification<Account> buildSpecification(
            String iban,
            Float balance,
            String typeOfAccount,
            UUID userId,
            LocalDate dateOfOpening,
            Boolean active) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (iban != null && !iban.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("iban"), "%" + iban + "%"));
            }
            if (balance != null) {
                predicates.add(criteriaBuilder.equal(root.get("balance"), balance));
            }
            if (typeOfAccount != null && !typeOfAccount.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("typeOfAccount"), AccountType.valueOf(typeOfAccount.toUpperCase())));
            }
            if (userId != null && !userId.toString().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("user"), userId));
            }
            if (dateOfOpening != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateOfOpening"), dateOfOpening));
            }
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }

}
