package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginResponseDTO responseDTO = userService.login(loginDTO);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        registerDTO.setRole(UserType.USER);
        UserDTO createdUserDTO = userService.register(registerDTO);
        return ResponseEntity.status(201).body(createdUserDTO);
    }
}
