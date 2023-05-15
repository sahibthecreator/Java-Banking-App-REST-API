package com.bank.app.restapi.controller;

import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "IBAN", required = false) String iban,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "minAmount", required = false) Float minAmount,
            @RequestParam(value = "maxAmount", required = false) Float maxAmount,
            @RequestParam(value = "exactAmount", required = false) Float exactAmount,
            @RequestParam(value = "typeOfTransaction", required = false) TransactionType typeOfTransaction) {

        List<Transaction> transactions = transactionService.getTransactions(userId, iban, startDate, endDate, minAmount, maxAmount, exactAmount, typeOfTransaction);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable UUID transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }
}

