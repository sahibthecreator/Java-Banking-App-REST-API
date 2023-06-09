package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.AccountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String iban;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Min(value = 0, message = "Balance must be greater than or equal to 0")
    private float balance;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "Type of account can't be null")
    private AccountType typeOfAccount;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "UserId can't be null")
    private UUID userId; // Add a separate userId field

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfOpening;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Min(value = 0, message = "Absolute Limit must be greater than or equal to 0")
    private float absoluteLimit;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private boolean active;
}
