package com.bank.app.restapi.controller;

import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateOfBirth,
            @RequestParam(required = false) String bsn,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<UserDTO> users = userService.getAll(firstName, lastName, email, dateOfBirth, bsn, role, sort, limit, offset);

        int statusCode = users.isEmpty() ? 204 : 200;

        return ResponseEntity.status(statusCode).body(users);
    }

    @PostMapping("")
    @PreAuthorize("@securityExpressions.hasEmployeeRole(authentication)")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid RegisterDTO registerDTO) {
        UserDTO createdUserDTO = userService.register(registerDTO);
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
    public ResponseEntity<HashMap<String, String>> deleteUser(@PathVariable String userId, HttpServletRequest request) {
        UUID id = UUID.fromString(userId);
        String response = userService.delete(id);
        HashMap<String, String> map = new HashMap<>();
        map.put("message", response);

        return ResponseEntity.status(200).body(map);
    }

    @GetMapping("/{userId}/remaining-day-limit")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(#userId, authentication)")
    public ResponseEntity<HashMap<String, Double>> getRemainingDayLimit(@PathVariable String userId,
            HttpServletRequest request) {
        UUID id = UUID.fromString(userId);
        Double response = userService.getRemainingDayLimit(id);
        HashMap<String, Double> map = new HashMap<>();
        map.put("remainingDayLimit", response);

        return ResponseEntity.status(200).body(map);
    }

}
