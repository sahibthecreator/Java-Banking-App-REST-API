package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<List<UserDTO>> getAll(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String bsn,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "20") int limit) {
        List<UserDTO> users = userService.getAll(firstName, lastName, email, dateOfBirth, bsn, sort, limit);

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUserDTO = userService.register(userDTO);
        return ResponseEntity.status(201).body(createdUserDTO);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(#userId, authentication)")
    public ResponseEntity<UserDTO> getById(@PathVariable String userId, HttpServletRequest request) {
        UUID id = UUID.fromString(userId);

        return ResponseEntity.status(200).body(userService.getUserDTOById(id));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(#userId, authentication)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody @Valid UserDTO userDTO,
            HttpServletRequest request) {

        UUID id = UUID.fromString(userId);
        UserDTO createdUserDTO = userService.update(id, userDTO);

        return ResponseEntity.status(200).body(createdUserDTO);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String userId, HttpServletRequest request) {
        UUID id = UUID.fromString(userId);
        userService.delete(id);

        return ResponseEntity.status(200).build();
    }

}
