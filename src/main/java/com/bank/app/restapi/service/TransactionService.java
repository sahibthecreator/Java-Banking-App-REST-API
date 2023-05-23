package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.TransactionRepository;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getTransactions(String iban, Float minAmount, Float maxAmount, Float exactAmount,
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
                    criteriaBuilder.function("date", LocalDate.class,
                            root.get("dateOfExecution")),
                    startDate));
        }

        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.function("date", LocalDate.class,
                            root.get("dateOfExecution")),
                    endDate));
        }
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found"));
    }

    public Transaction addTransaction(TransactionDTO dto, TransactionType type) {
        return transactionRepository.save(this.mapDtoToTransaction(dto, type));
    }

    private Transaction mapDtoToTransaction(TransactionDTO dto, TransactionType type) {
        Transaction t = new Transaction();
        t.setFromAccount(accountRepository.findByIban(dto.getFromAccount()));
        t.setToAccount(accountRepository.findByIban(dto.getToAccount()));
        t.setAmount(dto.getAmount());
        t.setTypeOfTransaction(type);
        t.setDateOfExecution(LocalDateTime.now());
        t.setPerformingUser(userRepository.findById(dto.getPerformingUser()).orElseThrow());
        t.setDescription(dto.getDescription());

        return t;
    }

}