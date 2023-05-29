package com.bank.app.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private  String iban;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float balance;

}
