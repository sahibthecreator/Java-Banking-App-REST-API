package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.AuthRequest;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.JwtService;
import com.bank.app.restapi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
         Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user credentials!");
        }

    }

    @PostMapping("/register")
    public ResponseEntity add_user(@RequestBody User u) {
        try {

            return ResponseEntity.status(201).body(
                    // custom http body, takes a success boolean and Class<T> as body
                    userService.register(u));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
