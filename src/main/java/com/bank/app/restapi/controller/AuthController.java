package com.bank.app.restapi.controller;

import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("auth")
public class AuthController {


    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // I know its auth controller not user, was just  testing!!!
    @GetMapping("/users")
    public ResponseEntity getAllUsers(){  
        try {
            List<User> users = this.userService.getAll();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("register")
    public ResponseEntity addUser(@RequestBody User user) {
        try {
            User newUser = this.userService.register(user);

            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
