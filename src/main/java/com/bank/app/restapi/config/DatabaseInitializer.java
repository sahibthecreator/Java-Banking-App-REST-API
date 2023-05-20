package com.bank.app.restapi.config;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.*;
import com.bank.app.restapi.repository.UserRepository;
import com.bank.app.restapi.service.AccountService;
import com.bank.app.restapi.service.TransactionService;
import com.bank.app.restapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public DatabaseInitializer(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
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

        
        user = userService.register(user);

        Account account1 = new Account();
        account1.setIban("12345678976543234567");
        account1.setBalance(20);
        account1.setTypeOfAccount(AccountType.CURRENT);
        account1.setUser(user);
        account1.setDateOfOpening(LocalDate.now());
        account1.setAbsoluteLimit(0);
        account1.setActive(true);

        accountService.createAccount(account1);

        Account account2 = new Account();
        account2.setIban("21000000000000000000");
        account2.setBalance(20);
        account2.setTypeOfAccount(AccountType.CURRENT);
        account2.setUser(user);
        account2.setDateOfOpening(LocalDate.now());
        account2.setAbsoluteLimit(0);
        account2.setActive(true);

        accountService.createAccount(account2);

        TransactionDTO transaction = new TransactionDTO();
        transaction.setFromAccount(account1.getIban());
        transaction.setToAccount(account2.getIban());
        transaction.setAmount(69);
        transaction.setPerformingUser(user.getId());
        transaction.setDescription("Bla bla");

        transactionService.addTransaction(transaction, TransactionType.DEPOSIT);
    }
}
