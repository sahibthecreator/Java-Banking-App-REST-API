package com.bank.app.restapi.controller;

import com.bank.app.restapi.http.HttpBody;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.JwtService;
import com.bank.app.restapi.service.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;

import org.hibernate.mapping.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<java.util.List<User>> getAll() {
        try {
            return ResponseEntity.status(200).body(service.getAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getById(@PathVariable UUID userId, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        JwtService jwtService = new JwtService();
        String email = jwtService.extractUsername(token);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasEmployeeAuthority = authentication != null
                && authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
        boolean isAuthorized = hasEmployeeAuthority || service.matchEmailwithId(email, userId);

        if (isAuthorized) {
            return ResponseEntity.status(200).body(service.findById(userId));
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable UUID userId, @RequestBody User u, HttpServletRequest request) {
        try {

            String token = extractTokenFromRequest(request);
            JwtService jwtService = new JwtService();
            String email = jwtService.extractUsername(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean hasEmployeeAuthority = authentication != null
                    && authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
            boolean isAuthorized = hasEmployeeAuthority || service.matchEmailwithId(email, userId);

            if (isAuthorized) {
                return ResponseEntity.status(201).body(service.update(userId, u));
            } else {
                return ResponseEntity.status(403).body(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable UUID userId, HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            JwtService jwtService = new JwtService();
            String email = jwtService.extractUsername(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean hasEmployeeAuthority = authentication != null
                    && authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
            boolean isAuthorized = hasEmployeeAuthority || service.matchEmailwithId(email, userId);

            if (isAuthorized) {
                service.delete(userId);
                return ResponseEntity.status(200).build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
