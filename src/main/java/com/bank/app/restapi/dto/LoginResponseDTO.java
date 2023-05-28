package com.bank.app.restapi.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponseDTO {
    private UUID userId;
    private String jwtToken;
}