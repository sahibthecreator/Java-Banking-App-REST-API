package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.AccountBalanceDTO;
import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.CustomerIbanDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.repository.AccountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    public List<AccountDTO> getAccounts( String iban,
                                         Float balance,
                                         String typeOfAccount,
                                         UUID userId,
                                         LocalDate dateOfOpening,
                                         boolean active,
                                         String sortDirection,
                                         int limit) {

        Specification<Account> specification = buildSpecification(iban, balance, typeOfAccount, userId, dateOfOpening, active);
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

        return accountList.stream().map(accountMapper::toDTO).toList()  ;
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {

        UUID userId = accountDTO.getUserId();
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new EntityNotFoundException("User with following id: " + userId + " not found");
        }
        else{
            if(accountDTO.getTypeOfAccount().equals(AccountType.SAVINGS)){
                if(!userHasCurrentAccount(userId)){
                    throw new IllegalArgumentException("User with following id: " + userId + " must have a current account to create a savings account ");
                }
            }

            setLimitsAccordingToUserType(user);
        }

        String dutchIban = generateUniqueDutchIban();

        accountDTO.setUserId(userId);
        accountDTO.setIban(dutchIban);
        accountDTO.setBalance(0);
        accountDTO.setDateOfOpening(LocalDate.now());
        accountDTO.setActive(true);

        Account account = accountMapper.toEntity(accountDTO);
        account = accountRepository.saveAndFlush(account);

        return accountMapper.toDTO(account);
    }

    public String deactivateAccount(String iban) {
        AccountDTO accountDTO = getAccountByIban(iban);
        if (updateAccountStatus(accountDTO, false, iban)){
            return "Account with iban: " + iban + " deactivated";
        } else {
            return "Account with iban: " + iban + " could not be deactivated";
        }

    }

    public String activateAccount(String iban) {
        AccountDTO accountDTO = getAccountByIban(iban);
        if (updateAccountStatus(accountDTO, true, iban)){
            return "Account with iban: " + iban + " activated";
        } else {
            return "Account with iban: " + iban + " could not be activated";
        }
    }

    public CustomerIbanDTO getIbanByUsername(String firstname, String lastname) {
        List<String> ibans =accountRepository.findIbanByFirstNameAndLastName(firstname, lastname);

        CustomerIbanDTO customerIbanDTO = new CustomerIbanDTO();
        customerIbanDTO.setFirstName(firstname);
        customerIbanDTO.setLastName(lastname);
        customerIbanDTO.setIbanList(ibans);

        if (ibans.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for user: " + firstname + " " + lastname);
        }
        return customerIbanDTO;
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
        if(account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        return accountMapper.toDTO(account);
    }

    public AccountBalanceDTO getBalanceByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if(account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        float balance = account.getBalance();
        AccountBalanceDTO balanceDTO = new AccountBalanceDTO();
        balanceDTO.setIban(iban);
        balanceDTO.setBalance(balance);

        return balanceDTO;
    }

    public boolean ibanExists(String dutchIban){
        Account account = accountRepository.findByIban(dutchIban);
        return account != null;
    }

    public String generateUniqueDutchIban() {

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

        while(ibanExists(iban)) {
            iban = generateUniqueDutchIban();
        }
        return iban;

    }


    // Private methods

    private boolean userHasCurrentAccount(UUID userId){
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        for (Account account : accounts) {
            if(account.getTypeOfAccount().equals(AccountType.CURRENT)){
                return true;
            }
        }
        return false;
    }
    private void setLimitsAccordingToUserType(User user){
        if(user.getRole().equals(UserType.USER)) {
            user.setRole(UserType.CUSTOMER);
            setLimits(user);
        }
        if(user.getRole().equals(UserType.EMPLOYEE)) {
            setLimits(user);
        }
    }
    private void setLimits(User user){
        user.setDayLimit(2000);
        user.setTransactionLimit(500);
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

    private boolean updateAccountStatus(AccountDTO accountDTO, boolean active, String iban) {
        if (accountDTO == null){
            throw new EntityNotFoundException("No account with the following iban "+iban);
        }
        Account account = accountMapper.toEntity(accountDTO);
        account.setActive(active);
        this.accountRepository.saveAndFlush(account);
        return true;
    }

    private Specification<Account> buildSpecification(String iban,
                                                      Float balance,
                                                      String typeOfAccount,
                                                      UUID userId,
                                                      LocalDate dateOfOpening,
                                                      boolean active) {
       return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (iban != null && !iban.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("iban"), "%" + iban + "%"));
            }
            if (balance != null) { 
                predicates.add(criteriaBuilder.equal(root.get("balance"), balance));
            }
            if (typeOfAccount != null && !typeOfAccount.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("typeOfAccount"), "%" + typeOfAccount + "%"));
            }
            if (userId != null && !userId.toString().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            if (dateOfOpening != null && !dateOfOpening.toString().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("dateOfOpening"), dateOfOpening));
            }
            if (active) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }

}
