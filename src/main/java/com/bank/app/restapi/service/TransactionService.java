package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.mapper.TransactionMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.TransactionRepository;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    private final Environment environment;


    private TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                              UserRepository userRepository, Environment environment, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.environment = environment;
        this.transactionMapper = transactionMapper;
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

    public TransactionDTO addTransaction(TransactionDTO dto, TransactionType type) {
        Transaction t = new Transaction();
        t.setFromAccount(accountRepository.findByIban(dto.getFromAccount()));
        t.setToAccount(accountRepository.findByIban(dto.getToAccount()));
        t.setAmount(dto.getAmount());
        t.setTypeOfTransaction(type);
        t.setDateOfExecution(LocalDateTime.now());
        t.setPerformingUser(userRepository.findById(dto.getPerformingUser()).orElseThrow());
        t.setDescription(dto.getDescription());

        deductMoneyFromAccount(t.getFromAccount(), t.getAmount());
        sentMoneyToAccount(t.getToAccount(), t.getAmount());

        Transaction savedTransaction = transactionRepository.save(t);
        return transactionMapper.toDTO(savedTransaction);
    }

    private void deductMoneyFromAccount(Account fromAccount, float amount) {
        checkAccountRelatedLimit(fromAccount, amount);

        float balance = fromAccount.getBalance();
        balance = balance - amount;
        fromAccount.setBalance(balance);
    }

    private void sentMoneyToAccount(Account toAccount, float amount) {
        float balance = toAccount.getBalance();
        balance = balance + amount;
        toAccount.setBalance(balance);
    }

    private void checkAccountRelatedLimit(Account fromAccount, float amount) {
        float balance = fromAccount.getBalance();
        balance = balance - amount;
        if (fromAccount.getAbsoluteLimit() > balance) {
            throw new IllegalArgumentException("Cannot exceed the account's absolute limit.");
        }
        if (amount > fromAccount.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds. Cannot complete transaction.");
        }
    }

}