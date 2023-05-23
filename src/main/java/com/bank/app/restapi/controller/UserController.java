package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.JwtService;
import com.bank.app.restapi.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private UserMapper userMapper;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<UserDTO> users = userService.getAll().stream().map(userMapper::toDTO).toList();

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.register(user);
        UserDTO createdUserDTO = userMapper.toDTO(createdUser);
        return ResponseEntity.status(201).body(createdUserDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getById(@PathVariable String userId, HttpServletRequest request) {
        UUID id = UUID.fromString(userId);
        if (isAuthorized(request, id)) {
            return ResponseEntity.status(200).body(userService.getUserDTOById(id));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {

        UUID id = UUID.fromString(userId);
        if (isAuthorized(request, id)) {
            User user = userMapper.toEntity(userDTO);
            UserDTO createdUserDTO = userMapper.toDTO(userService.update(id, user));

            return ResponseEntity.status(200).body(createdUserDTO);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    // User can't delete himself - TODO
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String userId, HttpServletRequest request) {
        UUID id = UUID.fromString(userId);
        if (isAuthorized(request, id)) {
            userService.delete(id);
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // To extract jwt token from header request
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // To check if user is either employee or its his own id
    private boolean isAuthorized(HttpServletRequest request, UUID userId) {
        String token = extractTokenFromRequest(request);
        JwtService jwtService = new JwtService();
        String email = jwtService.extractUsername(token);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isEmployee = authentication != null
                && authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
        boolean isAuthorized = isEmployee || userService.matchEmailwithId(email, userId);
        return isAuthorized;
    }

    // @GetMapping("*")
    // @PostMapping("*")
    // public ResponseEntity<?> handle404() {
    // return ResponseEntity.status(404).build(); // if something goes wrong return
    // 500 - internal server error
    // }

}
