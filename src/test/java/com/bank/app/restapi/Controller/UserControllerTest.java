package com.bank.app.restapi.Controller;

import com.bank.app.restapi.controller.UserController;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, null);
    }

    @Test
    void getAllShouldReturnListOfUsers() {
        List<UserDTO> userDTOList = Arrays.asList(new UserDTO(), new UserDTO());

        when(userService.getAll()).thenReturn(userDTOList);

        ResponseEntity<List<UserDTO>> response = userController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOList, response.getBody());
        verify(userService, times(1)).getAll();
    }

    @Test
    void createUserShouldReturnCreatedUser() {
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

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUserDTO, response.getBody());
        verify(userService, times(1)).register(userDTO);
    }

    @Test
    void getByIdShouldReturnUserWhenValidUserIdProvided() {
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();

        when(userService.getUserDTOById(id)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getById(id.toString(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserDTOById(id);
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        UserDTO createdUserDTO = new UserDTO();

        when(userService.update(id, userDTO)).thenReturn(createdUserDTO);

        ResponseEntity<?> response = userController.updateUser(id.toString(), userDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdUserDTO, response.getBody());
        verify(userService, times(1)).update(id, userDTO);
    }

    @Test
    void deleteUserShouldReturnHttpStatusOK() {
        UUID id = UUID.randomUUID();

        ResponseEntity<HttpStatus> response = userController.deleteUser(id.toString(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(id);
    }
}
