package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        List<User> userList = this.userRepository.findAll();

        return new ArrayList<UserDTO>(userList.stream().map(userMapper::toDTO).toList());
    }

    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID());
        user = this.userRepository.saveAndFlush(user);
        return userMapper.toDTO(user);
    }

    public String login(LoginDTO loginDTO) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        return jwtService.generateToken(loginDTO.getEmail());

    }

    public UserDTO update(UUID userId, UserDTO userDTO) throws EntityNotFoundException {
        User user = userMapper.toEntity(userDTO);
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            BeanUtils.copyProperties(user, existingUser, "id"); // Exclude copying the "id" property
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(existingUser);
            return userMapper.toDTO(savedUser);
        } else {
            throw new EntityNotFoundException("No user with following id " + userId + " exists");
        }
    }

    public boolean delete(UUID id) throws EntityNotFoundException {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        this.userRepository.deleteById(id);
        return true;
    }

    public boolean userIdExists(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public UserDTO getUserDTOById(UUID id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        return userMapper.toDTO(user.get());
    }
}
