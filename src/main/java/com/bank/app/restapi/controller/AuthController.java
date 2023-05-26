package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.UserDTO;
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
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        String JwtToken = userService.login(loginDTO);
        return ResponseEntity.status(201).body(JwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUserDTO = userService.register(userDTO);
        return ResponseEntity.status(201).body(createdUserDTO);
    }
}
