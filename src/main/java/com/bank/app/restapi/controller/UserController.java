package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.JwtService;
import com.bank.app.restapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private UserMapper userMapper;

    @GetMapping("/")
    public ResponseEntity getAll() {
        try {
            List<UserDTO> users = userService.getAll().stream().map(userMapper::toDTO).toList();

            return ResponseEntity.status(200).body(users);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build(); // if something goes wrong return 500 - internal server error
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            if (!userMapper.isValidDTO(userDTO)) {
                return ResponseEntity.status(400).body("Invalid request body"); // Return 400 for bad request
            }
            User user = userMapper.toEntity(userDTO);
            User createdUser = userService.register(user);
            UserDTO createdUserDTO = userMapper.toDTO(createdUser);
            return ResponseEntity.status(201).body(createdUserDTO);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(400).body("Invalid request body" + e.getMessage()); // Return 400 for bad
                                                                                                 // request
            } else {
                return ResponseEntity.status(500).body("An error occurred: " + e.getMessage()); // Return 500 Internal
                                                                                                // Server Error
            }
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<UserDTO>> getById(@PathVariable String userId, HttpServletRequest request) {
        if (!isValidUUID(userId)) {
            return ResponseEntity.status(400).build();
        }
        UUID id = UUID.fromString(userId);
        if (!userService.userIdExists(id)) {
            return ResponseEntity.status(404).build();
        }
        if (isAuthorized(request, id)) {
            return ResponseEntity.status(200).body(userService.findById(id).map(userMapper::toDTO));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        try {
            if (!userMapper.isValidDTO(userDTO)) {
                return ResponseEntity.status(400).body("Invalid request body"); // Return 400 for bad request
            }
            if (!isValidUUID(userId)) {
                return ResponseEntity.status(400).body("UserId is not valid"); // Return 400 for bad request
            }
            UUID id = UUID.fromString(userId);
            if (!userService.userIdExists(id)) {
                return ResponseEntity.status(404).build();
            }
            if (isAuthorized(request, id)) {
                User user = userMapper.toEntity(userDTO);
                UserDTO createdUserDTO = userMapper.toDTO(userService.update(id, user));

                return ResponseEntity.status(200).body(createdUserDTO);
            } else {
                return ResponseEntity.status(403).body(null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build(); // if something goes wrong return 500 - internal server error
        }
    }

    //User can't delete himself - TODO
    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String userId, HttpServletRequest request) {
        try {
            if (!isValidUUID(userId)) {
                return ResponseEntity.status(400).build();
            }
            UUID id = UUID.fromString(userId);
            if (!userService.userIdExists(id)) {
                return ResponseEntity.status(404).build();
            }
            if (isAuthorized(request, id)) {
                userService.delete(id);
                return ResponseEntity.status(200).build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build(); // if something goes wrong return 500 - internal server error=
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

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
