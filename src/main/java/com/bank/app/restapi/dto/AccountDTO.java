package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

public class AccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String iban;

    private float balance;

    private AccountType typeOfAccount;

    private User user;

    private String dateOfOpening;

    private float absoluteLimit;

    private boolean isActive;
}
