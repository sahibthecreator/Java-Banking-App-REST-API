package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.AccountBalanceDTO;
import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.AccountRequestDTO;
import com.bank.app.restapi.dto.CustomerIbanDTO;
import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.dto.mapper.AccountRequestMapper;
import com.bank.app.restapi.model.*;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.AccountRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountRequestRepository accountRequestRepository;

    @Autowired
    private final UserService userService;

    private final AccountMapper accountMapper;

    private final AccountRequestMapper accountRequestMapper;

    public List<AccountDTO> getAccounts(String iban,
            Float balance,
            String typeOfAccount,
            UUID userId,
            LocalDate dateOfOpening,
            boolean active,
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
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new EntityNotFoundException("User with following id: " + userId + " not found");
        } else {
            if (accountDTO.getTypeOfAccount().equals(AccountType.SAVINGS)) {
                if (!userHasCurrentAccount(userId)) {
                    throw new IllegalArgumentException("User with following id: " + userId
                            + " must have a current account to create a savings account ");
                }
            }

            setLimitsAccordingToUserType(user);
        }

        String dutchIban = generateUniqueDutchIban();

        accountDTO.setUserId(userId);
        accountDTO.setIban(dutchIban);
        accountDTO.setBalance(accountDTO.getBalance());
        accountDTO.setDateOfOpening(LocalDate.now());
        accountDTO.setActive(true);

        Account account = accountMapper.toEntity(accountDTO);
        account = accountRepository.saveAndFlush(account);

        return accountMapper.toDTO(account);
    }

    // The BANK's bank account
    public void createBankAccount(UUID id) {
        AccountDTO bank = new AccountDTO();
        bank.setIban("NL01INHO0000000001");
        bank.setBalance(1000000);
        bank.setTypeOfAccount(AccountType.CURRENT);
        bank.setUserId(id);
        bank.setDateOfOpening(LocalDate.now());
        bank.setAbsoluteLimit(0);
        bank.setActive(true);

        Account account = accountMapper.toEntity(bank);
        account = accountRepository.saveAndFlush(account);
    }

    public void updateAbsoluteLimit(String iban, AccountDTO accountDTO) throws EntityNotFoundException {
        Account existingAccount = accountRepository.findByIban(iban);
        if (existingAccount == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        existingAccount.setAbsoluteLimit(accountDTO.getAbsoluteLimit());
        accountRepository.save(existingAccount);
    }

    public String deactivateAccount(String iban) {
        AccountDTO accountDTO = getAccountDTOByIban(iban);
        if (updateAccountStatus(accountDTO, false, iban)) {
            return "Account with iban: " + iban + " deactivated";
        } else {
            return "Account with iban: " + iban + " could not be deactivated";
        }

    }

    public String activateAccount(String iban) {
        AccountDTO accountDTO = getAccountDTOByIban(iban);
        if (updateAccountStatus(accountDTO, true, iban)) {
            return "Account with iban: " + iban + " activated";
        } else {
            return "Account with iban: " + iban + " could not be activated";
        }
    }

    public List<CustomerIbanDTO> getIbanByUsername(String firstname, String lastname) {
        firstname = firstname == null ? "" : firstname.toLowerCase();
        lastname = lastname == null ? "" : lastname.toLowerCase();
        List<CustomerIbanDTO> customerIbanDTOs = accountRepository.findIbanByFirstNameAndLastName(firstname, lastname);
        return customerIbanDTOs;
    }

    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for user with id: " + userId);
        }
        return accounts.stream().map(accountMapper::toDTO).toList();
    }

    public AccountDTO getAccountDTOByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        return accountMapper.toDTO(account);
    }

    // I need this method for Transaction Services - Manol
    public Account getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        return account;
    }

    public AccountBalanceDTO getBalanceByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account with following iban: " + iban + " not found");
        }
        float balance = account.getBalance();
        AccountBalanceDTO balanceDTO = new AccountBalanceDTO();
        balanceDTO.setIban(iban);
        balanceDTO.setBalance(balance);

        return balanceDTO;
    }

    public boolean ibanExists(String dutchIban) {
        Account account = accountRepository.findByIban(dutchIban);
        return account != null;
    }

    public String generateUniqueDutchIban() {
        String accountNumber = String.valueOf((int) (Math.random() * 1000000000));

        String countryCode = "NL";
        String checkDigitPlaceholder = "00";
        String customCode = "INHO";

        String incompleteIban = countryCode + checkDigitPlaceholder + accountNumber;
        String checkDigit = calculateCheckDigit(incompleteIban);

        String iban = countryCode + checkDigit + customCode + accountNumber;

        while (ibanExists(iban)) {
            iban = generateUniqueDutchIban();
        }
        return iban;
    }

    public AccountRequestDTO submitAccountRequest(AccountRequestDTO requestDto) {
        User user = userService.getUserById(requestDto.getUserId());
        AccountRequest request = new AccountRequest();
        request.setUserId(requestDto.getUserId());
        request.setFullName(user.getFirstName() + " " + user.getLastName());
        request.setAccountType(requestDto.getAccountType());
        request.setStatus("pending");

        return accountRequestMapper.toDTO(accountRequestRepository.save(request));
    }

    public List<AccountRequestDTO> getAllRequests() {

        List<AccountRequest> accountRequests = accountRequestRepository.findAll();
        return accountRequests.stream().map(accountRequestMapper::toDTO).toList();
    }

    public AccountDTO approveAccountRequest(UUID requestId) {
        AccountRequest request = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + requestId + " does not exist"));

        request.setStatus("accepted");
        accountRequestRepository.save(request);
        AccountDTO accountDTO = AccountDTO.builder()
                .balance(0)
                .typeOfAccount(request.getAccountType())
                .userId(request.getUserId())
                .absoluteLimit(0)
                .build();

        return createAccount(accountDTO);
    }

    public AccountRequestDTO denyBankAccountRequest(UUID requestId) {
        AccountRequest request = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + requestId + " does not exist"));
        request.setStatus("denied");
        accountRequestRepository.save(request);
        return accountRequestMapper.toDTO(request);
    }

    public boolean updateAccountStatus(AccountDTO accountDTO, boolean active, String iban) {
        if (accountDTO == null) {
            throw new EntityNotFoundException("No account with the following iban " + iban);
        }
        Account account = accountMapper.toEntity(accountDTO);
        account.setActive(active);
        this.accountRepository.saveAndFlush(account);
        return true;
    }

    // Private methods
    private boolean userHasCurrentAccount(UUID userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        for (Account account : accounts) {
            if (account.getTypeOfAccount().equals(AccountType.CURRENT)) {
                return true;
            }
        }
        return false;
    }

    private void setLimitsAccordingToUserType(User user) {
        if (user.getRole().equals(UserType.USER)) {
            user.setRole(UserType.CUSTOMER);
            setLimits(user);
        }
        if (user.getRole().equals(UserType.EMPLOYEE)) {
            setLimits(user);
        }
    }

    private void setLimits(User user) {
        user.setDayLimit(user.getDayLimit() != 0 ? user.getDayLimit() : 100);
        user.setTransactionLimit(user.getTransactionLimit() != 0 ? user.getTransactionLimit() : 50);
    }

    private String calculateCheckDigit(String iban) {

        String moved = iban.substring(4) + iban.substring(0, 4);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < moved.length(); i++) {
            char c = moved.charAt(i);
            if (Character.isLetter(c)) {
                sb.append(Character.getNumericValue(c));
            } else {
                sb.append(c);
            }
        }

        java.math.BigInteger bigInt = new java.math.BigInteger(sb.toString());
        java.math.BigInteger remainder = bigInt.mod(java.math.BigInteger.valueOf(97));
        BigInteger checkDigit = BigInteger.valueOf(98).subtract(remainder);
        return String.format("%02d", checkDigit);
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
                predicates.add(criteriaBuilder.equal(root.get("user"), userId));
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
