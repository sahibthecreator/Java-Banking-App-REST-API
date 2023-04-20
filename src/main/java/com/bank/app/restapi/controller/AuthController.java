package com.bank.app.restapi.controller;

import com.bank.app.restapi.http.HttpBody;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthController {


    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody ObjectNode json){
        boolean verification = userService.verifyCredentials(json.get("email").asText(), json.get("passwd").asText());

        //return forbidden if user is not correct
        if(!verification){
            return ResponseEntity.status(401).body(
                    new HttpBody<>(false, "Access forbidden")
            );
        }

        return  ResponseEntity.status(200).body(
                //custom http body, takes a success boolean and Class<T> as body
                new HttpBody<>(true, "Successful login"));
    }

    @PostMapping("/register")
    public ResponseEntity add_user(@RequestBody User u){
        try{
            return  ResponseEntity.status(201).body(
                    //custom http body, takes a success boolean and Class<T> as body
                    new HttpBody<>(true, userService.register(u)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
