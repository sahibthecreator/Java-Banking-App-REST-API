package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.mapper.TransactionMapper;
import com.bank.app.restapi.model.*;
import com.bank.app.restapi.repository.TransactionRepository;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private UserRepository userRepository;

    private final Environment environment;

    private TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService,
                              UserRepository userRepository, Environment environment, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.environment = environment;
        this.transactionMapper = transactionMapper;
    }

    private List<TransactionDTO> getTodaysTransactionsFromSendingUser(String iban, LocalDate today) {
        Specification<Transaction> specification = Specification.where(null);

        if (iban != null && !iban.isEmpty() && today != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("fromAccount").get("iban"), iban));

            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("dateOfExecution").as(Date.class), Date.valueOf(today)));

            List<Transaction> transactions = transactionRepository.findAll(specification);
            return transactions.stream().map(transactionMapper::toDTO).toList();
        }

        throw new RuntimeException("Internal error. Could not retrieve IBAN and/or Today's date.");
    }

    public List<TransactionDTO> getTransactions(String iban, Float minAmount, Float maxAmount, Float exactAmount,
            TransactionType typeOfTransaction, LocalDate startDate, LocalDate endDate) {

        Specification<Transaction> specification = Specification.where(null);

        if (iban != null && !iban.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("fromAccount").get("iban"), iban),
                    criteriaBuilder.equal(root.get("toAccount").get("iban"), iban)));
        }

        if (exactAmount != null) {
            specification = specification
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("amount"), exactAmount));
        } else if (minAmount != null && maxAmount != null) {
            if (minAmount <= maxAmount) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                        .between(root.get("amount"), minAmount, maxAmount));
            } else {
                throw new IllegalArgumentException("minAmount should be less than or equal to maxAmount.");
            }
        } else if (minAmount != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("amount"), minAmount));
        } else if (maxAmount != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount));
        }

        if (typeOfTransaction != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("typeOfTransaction"), typeOfTransaction));
        }

        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dateOfExecution").as(Date.class),
                    Date.valueOf(startDate)));
        }

        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                    root.get("dateOfExecution").as(Date.class),
                    Date.valueOf(endDate)));
        }

        int defaultLimit = Integer.parseInt(environment.getProperty("api.transaction.default-limit", "100"));

        List<Transaction> transactions = transactionRepository.findAll(specification);
        return transactions.stream().map(transactionMapper::toDTO).limit((defaultLimit)).toList();

    }

    public TransactionDTO getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).map(transactionMapper::toDTO).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found"));
    }

    public List<TransactionDTO> getTransactionsByUserId(UUID userId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByUserId(userId);

        // Convert the list of Transaction entities to TransactionDTOs
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::toDTO).toList();

        return transactionDTOs;
    }

    public TransactionDTO addTransaction(TransactionDTO dto, TransactionType type) {
        Transaction t = new Transaction();
        t.setFromAccount(validateAccountsBasedOnTransactionType(
                accountService.getAccountByIban(dto.getFromAccount()),
                true,
                type));
        t.setToAccount(validateAccountsBasedOnTransactionType(
                accountService.getAccountByIban(dto.getToAccount()),
                false,
                type));
        checkSelfTransaction(t.getFromAccount(), t.getToAccount());
        t.setAmount(validateAmount(dto.getAmount()));
        t.setTypeOfTransaction(type);
        t.setDateOfExecution(LocalDateTime.now());
        t.setPerformingUser(userRepository.findById(dto.getPerformingUser()).orElseThrow(
                () -> new EntityNotFoundException("Performing user not found")));
        t.setDescription(dto.getDescription());

        deductMoneyFromAccount(t.getFromAccount(), t.getAmount());
        sentMoneyToAccount(t.getToAccount(), t.getAmount());

        Transaction savedTransaction = transactionRepository.save(t);
        return transactionMapper.toDTO(savedTransaction);
    }

    private void deductMoneyFromAccount(Account fromAccount, float amount) {
        checkAccountRelatedLimits(fromAccount, amount);
        checkCustomerRelatedLimits(fromAccount, amount);

        float balance = fromAccount.getBalance();
        balance = balance - amount;
        fromAccount.setBalance(balance);
    }

    private void sentMoneyToAccount(Account toAccount, float amount) {
        if (!toAccount.isActive()) {
            throw new IllegalArgumentException("Receiving account is deactivated.");
        }
        float balance = toAccount.getBalance();
        balance = balance + amount;
        toAccount.setBalance(balance);
    }

    private float validateAmount(float amount) {
        if (Float.isNaN(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number.");
        }

        return amount;
    }

    private Account validateAccountsBasedOnTransactionType(Account accountToVerify, boolean accountToVerifyIsSending ,TransactionType transactionType) {
        switch (transactionType) {
            case DEPOSIT:
                if (!accountToVerify.equals(accountService.getAccountByIban("NL01INHO0000000001")) && !accountToVerifyIsSending) {
                    throw new AccessDeniedException("During deposit, transaction cannot be sent to an account other than the BANK's dedicated one");
                }
                validateAccountsBasedOnAccountType(accountToVerify, transactionType);
                break;
            case WITHDRAWAL:
                if (!accountToVerify.equals(accountService.getAccountByIban("NL01INHO0000000001")) && accountToVerifyIsSending) {
                    throw new AccessDeniedException("During withdrawal, transaction cannot be sent from an account other than the BANK's dedicated one");
                }
                validateAccountsBasedOnAccountType(accountToVerify, transactionType);
                break;
            case TRANSFER:
                break;
            default:
                throw new IllegalArgumentException("Undefined type of transaction");
        }

        return accountToVerify;
    }

    private void validateAccountsBasedOnAccountType(Account accountToVerify, TransactionType transactionType) {
        if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.WITHDRAWAL) {
            if (accountToVerify.getTypeOfAccount() == AccountType.SAVINGS) {
                throw new IllegalArgumentException("Cannot perform " + transactionType.name() + " transaction, involving a savings account");
            }
        } else if (transactionType == TransactionType.TRANSFER) {
            //TODO: Allow transfer from/to SAVINGS account only on the same user
        }
    }

    private void checkSelfTransaction(Account fromAccount, Account toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new AccessDeniedException("Cannot make transactions to self.");
        }
    }

    private void checkAccountRelatedLimits(Account fromAccount, float amount) {
        float balance = fromAccount.getBalance();
        balance = balance - amount;
        if (!fromAccount.isActive()) {
            throw new IllegalArgumentException("Sending account is deactivated.");
        }
        if (fromAccount.getAbsoluteLimit() > balance) {
            throw new IllegalArgumentException("Cannot exceed the account's absolute limit.");
        }
        if (amount > fromAccount.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds. Cannot complete transaction.");
        }
    }

    private void checkCustomerRelatedLimits(Account fromAccount, float amount) {
        User ownerOfSendingAccount = mapAccountIbanToOwner(fromAccount);

        if (amount > ownerOfSendingAccount.getTransactionLimit()) {
            throw new IllegalArgumentException("Transaction exceed the transaction limit.");
        }

        checkCustomerDailyLimit(ownerOfSendingAccount.getDayLimit(), fromAccount.getIban(), amount);
    }

    private void checkCustomerDailyLimit (float dayLimit, String ownerIban, float amountToSend) {
        float ownerDayLimit = dayLimit;

        List<TransactionDTO> todaysTransactionsFromSendingUser = this.getTodaysTransactionsFromSendingUser(ownerIban, LocalDate.now());
        float sentMoneyToday = 0;
        for (TransactionDTO t:
             todaysTransactionsFromSendingUser) {
            sentMoneyToday = sentMoneyToday + t.getAmount();
        }

        if (sentMoneyToday + amountToSend > ownerDayLimit) {
            throw new IllegalArgumentException("Day limit has been reached.");
        }

    }

    private User mapAccountIbanToOwner(Account account) {
        Optional<User> ownerOfSendingAccount = userRepository.findById(account.getUser().getId());

        return ownerOfSendingAccount.get();
    }

}