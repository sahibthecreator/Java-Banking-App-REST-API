package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> getAll() {
        return new ArrayList<User>(this.userRepository.findAll());
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID());
        return this.userRepository.saveAndFlush(user);
    }

    public User update(UUID userId, User user) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(userId);
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                BeanUtils.copyProperties(user, existingUser, "id"); // Exclude copying the "id" property
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

                User savedUser = userRepository.save(existingUser);
                return savedUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(UUID id) {
        this.userRepository.deleteById(id);
        return true;
    }

    public boolean matchEmailwithId(String email, UUID id) {
        return userRepository.matchEmailwithId(email, id);
    }

    public boolean userIdExists(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public UserDTO getUserDTOById(UUID id) {
        User user = userRepository.findById(id).get();
        return userMapper.toDTO(user);
    }

    public User getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
