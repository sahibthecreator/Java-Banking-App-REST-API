package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String fromAccount;

    private String toAccount;

    private UUID performingUser;

    private float amount;
    
    private TransactionType typeOfTransaction;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateOfExecution;

    private String description;
}
