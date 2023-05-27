package com.bank.app.restapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
public class LoginDTO {

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @NotBlank(message = "Password can't be empty")
    private String password;
}