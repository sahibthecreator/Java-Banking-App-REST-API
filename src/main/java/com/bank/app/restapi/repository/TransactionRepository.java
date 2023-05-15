package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByPerformingUser_Id(UUID userId);
    List<Transaction> findByFromIban_IbanOrToIban_Iban(String fromIban, String toIban);
    List<Transaction> findByDateOfExecutionBetween(LocalDate startDate, LocalDate endDate);
    List<Transaction> findByAmountGreaterThanEqual(Float minAmount);
    List<Transaction> findByAmountLessThanEqual(Float maxAmount);
    List<Transaction> findByAmount(Float exactAmount);
    List<Transaction> findByTypeOfTransaction(TransactionType typeOfTransaction);
    Optional<Transaction> findById(UUID transactionId);
}

