package com.bank.app.restapi.Controller;

import com.bank.app.restapi.controller.AuthController;
import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.UUID;

class AuthControllerTest {

    private AuthController authController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userService);
    }

    @Test
    void loginShouldReturnJwtToken() {
        LoginDTO loginDTO = new LoginDTO("sam@gmail.com", "11111");
        String expectedJwtToken = "jwt-token";

        when(userService.login(loginDTO)).thenReturn(expectedJwtToken);

        ResponseEntity<String> response = authController.login(loginDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedJwtToken, response.getBody());
        verify(userService, times(1)).login(loginDTO);
    }

    @Test
    void registerShouldReturnCreatedUserDTO() {
        UserDTO userDTO = UserDTO.builder()
                .firstName("Sam")
                .lastName("Jhonson")
                .email("sam@gmail.com")
                .password("11111")
                .dateOfBirth(LocalDate.of(2004, 3, 23))
                .role(UserType.USER)
                .build();

        UserDTO createdUserDTO = UserDTO.builder()
                .id(UUID.randomUUID())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password("hashedPassword")
                .bsn(userDTO.getBsn())
                .dateOfBirth(userDTO.getDateOfBirth())
                .role(userDTO.getRole())
                .build();

        when(userService.register(userDTO)).thenReturn(createdUserDTO);

        ResponseEntity<UserDTO> response = authController.register(userDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUserDTO, response.getBody());
        verify(userService, times(1)).register(userDTO);
    }
}
