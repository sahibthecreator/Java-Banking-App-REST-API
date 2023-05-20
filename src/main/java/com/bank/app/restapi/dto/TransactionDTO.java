package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String fromAccount;
    private String toAccount;
    private float amount;
    private UUID performingUser;
    private String description;
}
