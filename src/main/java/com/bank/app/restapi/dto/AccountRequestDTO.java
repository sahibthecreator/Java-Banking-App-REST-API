package com.bank.app.restapi.dto;

import java.util.UUID;

import com.bank.app.restapi.model.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "userId can't be null")
    private UUID userId;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "Account type can't be null")
    private AccountType accountType;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;
}
