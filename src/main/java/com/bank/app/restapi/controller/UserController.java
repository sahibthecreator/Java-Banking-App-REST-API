package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getAll() {
        List<UserDTO> users = userService.getAll().stream().map(userMapper::toDTO).toList();

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.register(user);
        UserDTO createdUserDTO = userMapper.toDTO(createdUser);
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
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {

        UUID id = UUID.fromString(userId);
        User user = userMapper.toEntity(userDTO);
        UserDTO createdUserDTO = userMapper.toDTO(userService.update(id, user));

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
