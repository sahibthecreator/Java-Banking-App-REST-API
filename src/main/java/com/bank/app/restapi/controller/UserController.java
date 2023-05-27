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
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAll();

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
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody @Valid UserDTO userDTO,
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
