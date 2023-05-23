package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.AccountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //Removed read-only to test transactions
    private String iban;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private float balance;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private AccountType typeOfAccount;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private UUID userId; // Add a separate userId field

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfOpening;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private float absoluteLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isActive;
}
