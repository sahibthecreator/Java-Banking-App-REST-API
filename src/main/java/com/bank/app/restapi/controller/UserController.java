package com.bank.app.restapi.controller;

import com.bank.app.restapi.http.HttpBody;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.service.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity all_users(){
        try{
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, service.getAll()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //allow reading of headers to retrieve token
    @RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "/get_all_with_token")
    @ResponseBody
    public ResponseEntity get_all_users_with_token(HttpServletRequest request) {
        String token = request.getHeader("jwt_token");

        //TODO: implement token verification
        if(!token.equals("correct_token")){
            return ResponseEntity.status(403).body(
                    new HttpBody<>(false, "Invalid token")
            );
        }


        //return desired result
        return ResponseEntity.status(200).body(
                new HttpBody<>(true, service.getAll())
        );
    }

    @DeleteMapping("/delete_user/{userId}")
    public ResponseEntity<Object> delete_user(@PathVariable UUID userId){
        try{
            service.delete(userId);
            return ResponseEntity.status(200).body(
                    new HttpBody<>(true, userId)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //retrieve info by user_id
    @GetMapping("/user_info/{userId}")
    public ResponseEntity<Object> user_info_by_id(@PathVariable UUID userId){
            return  ResponseEntity.status(200).body(
                    //custom http body, takes a success boolean and Class<T> as body
                    new HttpBody<>(true, service.findById(userId)));
    }

}

