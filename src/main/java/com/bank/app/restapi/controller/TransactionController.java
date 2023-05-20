package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "minAmount", required = false) Float minAmount,
            @RequestParam(value = "maxAmount", required = false) Float maxAmount,
            @RequestParam(value = "exactAmount", required = false) Float exactAmount,
            @RequestParam(value = "typeOfTransaction", required = false) TransactionType typeOfTransaction) {

        List<Transaction> transactions = transactionService.getTransactions(iban, minAmount, maxAmount, exactAmount, typeOfTransaction, startDate, endDate);

        return ResponseEntity.status(200).body(transactions);
    }

    @GetMapping(value = "{transactionId}")
    public ResponseEntity getTransactionById(@PathVariable UUID transactionId) {
        Transaction result = transactionService.getTransactionById(transactionId);
        return ResponseEntity.status(200).body(result);
    }

    @PostMapping(value = {"", "{mappingNameHolder}", "{mappingNameHolder}/"})
    public ResponseEntity addTransaction(@PathVariable(required = false) String mappingNameHolder, @RequestBody TransactionDTO transaction) {
        TransactionType typeOfTransaction;
        try {
            if (mappingNameHolder == null || mappingNameHolder.isEmpty()) {
                typeOfTransaction = TransactionType.TRANSFER;
            } else if (mappingNameHolder.equals("deposit")) {
                typeOfTransaction = TransactionType.DEPOSIT;
            } else if (mappingNameHolder.equals("withdraw")) {
                typeOfTransaction = TransactionType.WITHDRAWAL;
            } else {
                throw new IllegalArgumentException("Invalid value for mappingNameHolder in the URI");
            }
            return ResponseEntity.status(201).body(transactionService.addTransaction(transaction, typeOfTransaction));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("Invalid value for mappingNameHolder")) {
                return ResponseEntity.status(400).body("Invalid URI: " + e.getMessage());
            } else {
                return ResponseEntity.status(400).body("Invalid request body: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}