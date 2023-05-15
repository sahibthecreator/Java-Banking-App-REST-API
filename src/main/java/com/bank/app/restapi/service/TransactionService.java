package com.bank.app.restapi.service;

import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactions(UUID userId, String iban, LocalDate startDate, LocalDate endDate, Float minAmount, Float maxAmount, Float exactAmount, TransactionType typeOfTransaction) {

        List<Transaction> transactions = transactionRepository.findAll();

        // Filter based on the provided parameters
        if (userId != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getPerformingUser().getId().equals(userId))
                    .collect(Collectors.toList());
        }

        if (iban != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getFromIban().getIban().equals(iban) || transaction.getToIban().getIban().equals(iban))
                    .collect(Collectors.toList());
        }

        if (startDate != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getDateOfExecution().toLocalDate().isEqual(startDate) || transaction.getDateOfExecution().toLocalDate().isAfter(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getDateOfExecution().toLocalDate().isEqual(endDate) || transaction.getDateOfExecution().toLocalDate().isBefore(endDate))
                    .collect(Collectors.toList());
        }

        if (minAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() >= minAmount)
                    .collect(Collectors.toList());
        }

        if (maxAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() <= maxAmount)
                    .collect(Collectors.toList());
        }

        if (exactAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() == exactAmount)
                    .collect(Collectors.toList());
        }

        if (typeOfTransaction != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getTypeOfTransaction().equals(typeOfTransaction))
                    .collect(Collectors.toList());
        }

        return transactions;
    }

    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElse(null);
    }

}


