package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //Removed read-only to test transactions
    private String iban;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float balance;

    private AccountType typeOfAccount;

    private User user;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfOpening;

    private float absoluteLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isActive;
}
