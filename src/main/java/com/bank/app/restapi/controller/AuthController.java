package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.JwtService;
import com.bank.app.restapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO authRequest) {
        try {
            if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
                return ResponseEntity.status(400).body("Invalid request body data"); // Return 400 for bad request
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return ResponseEntity.status(200).body(jwtService.generateToken(authRequest.getUsername()));
            } else {
                return ResponseEntity.status(401).body("Invalid username or password"); // Return 400 for bad request
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password"); // Return 400 for bad request

        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            userMapper.isValidDTO(userDTO);
            User user = userMapper.toEntity(userDTO);
            User createdUser = userService.register(user);
            UserDTO createdUserDTO = userMapper.toDTO(createdUser);
            return ResponseEntity.status(201).body(createdUserDTO);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(400).body("Invalid data: " + e.getMessage()); // Return 400 for bad request
            } else {
                return ResponseEntity.status(500).body("An error occurred: " + e.getMessage()); // Return 500 Internal
                                                                                                // Server Error
            }
        }
    }
}
