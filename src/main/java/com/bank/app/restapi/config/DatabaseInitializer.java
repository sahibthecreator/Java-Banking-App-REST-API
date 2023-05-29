package com.bank.app.restapi.config;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.TransactionType;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.AccountService;
import com.bank.app.restapi.service.TransactionService;
import com.bank.app.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final UserMapper userMapper;

    @Autowired
    public DatabaseInitializer(UserService userService, AccountService accountService,
            TransactionService transactionService, UserMapper userMapper) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.userMapper = userMapper;
    }

    @Override
    public void run(String... args) throws ParseException {
        User user = new User();
        user.setFirstName("Root");
        user.setLastName("Admin");
        user.setEmail("root@gmail.com");
        user.setPassword("11111");
        user.setBsn("212121212");
        user.setDateOfBirth(LocalDate.of(2004, 3, 23));
        user.setRole(UserType.EMPLOYEE);
        user.setDayLimit(50);
        user.setTransactionLimit(100);


        UserDTO userDto = userService.register(userMapper.toDTO(user));

        AccountDTO account1 = new AccountDTO();
        account1.setBalance(500);
        account1.setTypeOfAccount(AccountType.CURRENT);
        account1.setUserId(userDto.getId());
        account1.setDateOfOpening(LocalDate.now());
        account1.setAbsoluteLimit(200);
        account1.setActive(true);

        accountService.createAccount(account1);


        AccountDTO account2 = new AccountDTO();
        account2.setBalance(0);
        account2.setTypeOfAccount(AccountType.CURRENT);
        account2.setUserId(userDto.getId());
        account2.setDateOfOpening(LocalDate.now());
        account2.setAbsoluteLimit(50);
        account2.setActive(true);

        accountService.createAccount(account2);

        AccountDTO account3 = new AccountDTO();
        account3.setBalance(200);
        account3.setTypeOfAccount(AccountType.CURRENT);
        account3.setUserId(userDto.getId());
        account3.setDateOfOpening(LocalDate.now());
        account3.setAbsoluteLimit(50);
        account3.setActive(true);

        accountService.createAccount(account3);

        TransactionDTO transaction = new TransactionDTO();
        transaction.setFromAccount(account1.getIban());
        transaction.setToAccount(account2.getIban());
        transaction.setAmount(20);
        transaction.setPerformingUser(userDto.getId());
        transaction.setTypeOfTransaction(TransactionType.TRANSFER);
        transaction.setDescription("Bla bla");

        transactionService.addTransaction(transaction, TransactionType.DEPOSIT);
        


        TransactionDTO transaction1 = new TransactionDTO();
        transaction1.setFromAccount(account3.getIban());
        transaction1.setToAccount(account2.getIban());
        transaction1.setAmount(30);
        transaction1.setPerformingUser(userDto.getId());
        transaction1.setTypeOfTransaction(TransactionType.TRANSFER);
        transaction1.setDescription("Second test transaction");

        transactionService.addTransaction(transaction1, TransactionType.DEPOSIT);
    }
}