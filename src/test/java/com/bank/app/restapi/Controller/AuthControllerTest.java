 package com.bank.app.restapi.controller;

 import com.bank.app.restapi.controller.AuthController;
 import com.bank.app.restapi.dto.LoginDTO;
 import com.bank.app.restapi.dto.LoginResponseDTO;
 import com.bank.app.restapi.dto.RegisterDTO;
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
         LoginResponseDTO responseDTO = new LoginResponseDTO(UUID.randomUUID(), expectedJwtToken);

         when(userService.login(loginDTO)).thenReturn(responseDTO);

         ResponseEntity<LoginResponseDTO> response = authController.login(loginDTO);

         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertEquals(responseDTO, response.getBody());
         assertEquals(expectedJwtToken, response.getBody().getJwtToken());
         verify(userService, times(1)).login(loginDTO);
     }

     @Test
     void registerShouldReturnCreatedUserDTO() {
         RegisterDTO registerDTO = RegisterDTO.builder()
                 .firstName("Sam")
                 .lastName("Jhonson")
                 .email("sam@gmail.com")
                 .password("11111")
                 .dateOfBirth(LocalDate.of(2004, 3, 23))
                 .build();

         UserDTO createdUserDTO = UserDTO.builder()
                 .id(UUID.randomUUID())
                 .firstName(registerDTO.getFirstName())
                 .lastName(registerDTO.getLastName())
                 .email(registerDTO.getEmail())
                 .password("hashedPassword")
                 .bsn(registerDTO.getBsn())
                 .dateOfBirth(registerDTO.getDateOfBirth())
                 .role(UserType.USER)
                 .build();

         when(userService.register(registerDTO)).thenReturn(createdUserDTO);

         ResponseEntity<UserDTO> response = authController.register(registerDTO);
         assertEquals(HttpStatus.CREATED, response.getStatusCode());
         assertEquals(createdUserDTO, response.getBody());
         verify(userService, times(1)).register(registerDTO);
     }
 }
