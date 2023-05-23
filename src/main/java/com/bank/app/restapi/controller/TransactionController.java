package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(value = "minAmount", required = false) Float minAmount,
            @RequestParam(value = "maxAmount", required = false) Float maxAmount,
            @RequestParam(value = "exactAmount", required = false) Float exactAmount,
            @RequestParam(value = "typeOfTransaction", required = false) TransactionType typeOfTransaction) {

        List<TransactionDTO> transactions = transactionService.getTransactions(iban, minAmount, maxAmount, exactAmount,
                typeOfTransaction, startDate, endDate);

        return ResponseEntity.status(200).body(transactions);
    }

    @GetMapping(value = "{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID transactionId) {
        TransactionDTO result = transactionService.getTransactionById(transactionId);
        return ResponseEntity.status(200).body(result);
    }

    @PostMapping(value = { "", "{mappingNameHolder}", "{mappingNameHolder}/" })
    public ResponseEntity<TransactionDTO> addTransaction(@PathVariable(required = false) String mappingNameHolder,
            @RequestBody TransactionDTO transaction) {
        TransactionType typeOfTransaction;
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
    }
}