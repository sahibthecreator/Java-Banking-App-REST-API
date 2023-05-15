package com.bank.app.restapi.dto;

import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {


    @JsonIgnore
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;


    @JsonProperty
    @JsonIgnore
    private String password;

    private String bsn;

    private LocalDate dateOfBirth;

    private UserType role;

    private float dayLimit;
    private float transactionLimit;


    @JsonIgnore
    private Set<Account> accounts = new HashSet<>();
}