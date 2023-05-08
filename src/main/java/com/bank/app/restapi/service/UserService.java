package com.bank.app.restapi.service;

import com.bank.app.restapi.model.User;
import com.bank.app.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return new ArrayList<User>(this.userRepository.findAll());
    }

    public User register(User user) {

        //append uuid to user
        user.setUuid(UUID.randomUUID());
        return this.userRepository.saveAndFlush(user);
    }

    public boolean delete(UUID uuid){
        this.userRepository.deleteByUuid(uuid);
        return true;
    }

    public boolean verifyCredentials(String email, String passwd) {
        //TODO: implement credential verification
        return true;
    }

    public User findById(UUID id){
        return userRepository.findByUuid(id);
    }
}