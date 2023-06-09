 package com.bank.app.restapi.controller;

  import com.bank.app.restapi.controller.UserController;
  import com.bank.app.restapi.dto.RegisterDTO;
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
  import java.util.HashMap;
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
          userController = new UserController(userService);
      }

      @Test
      void getAllShouldReturnListOfUsers() {
          List<UserDTO> userDTOList = Arrays.asList(new UserDTO(), new UserDTO());

          when(userService.getAll(null, null, null, null, null, null, null, 10, 0)).thenReturn(userDTOList);

          ResponseEntity<List<UserDTO>> response = userController.getAll(null, null, null, null, null, null, null, 10, 0);
          assertEquals(HttpStatus.OK, response.getStatusCode());
          assertEquals(userDTOList, response.getBody());
          verify(userService, times(1)).getAll(null, null, null, null, null, null, null, 10, 0);
      }

      @Test
      void createUserShouldReturnCreatedUser() {
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

          ResponseEntity<UserDTO> response = userController.createUser(registerDTO);

          assertEquals(HttpStatus.CREATED, response.getStatusCode());
          assertEquals(createdUserDTO, response.getBody());
          verify(userService, times(1)).register(registerDTO);
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

          ResponseEntity<HashMap<String, String>> response = userController.deleteUser(id.toString(), request);

          assertEquals(HttpStatus.OK, response.getStatusCode());
          verify(userService, times(1)).delete(id);
      }
  }