package com.bank.app.restapi.service;

import com.bank.app.restapi.model.User;
import com.bank.app.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<User>();

        this.userRepository.findAll().forEach(users::add);

        return users;
    }

    public User register(User user) {
        return this.userRepository.saveAndFlush(user);
    }
}
