package com.bank.app.restapi.config;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.repository.UserRepository;
import com.bank.app.restapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DatabaseInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        User user = new User();
        user.setFirstName("Root");
        user.setLastName("Admin");
        user.setEmail("root@gmail.com");
        user.setPassword("11111");
        user.setBsn("212121212");
        user.setDateOfBirth(LocalDate.of(2004, 3, 23));
        user.setRole(UserType.EMPLOYEE);

        
        userService.register(user);
    }
}
