package com.bank.app.restapi.config;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountType;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.service.AccountService;
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
        private final UserMapper userMapper;

        @Autowired
        public DatabaseInitializer(UserService userService, AccountService accountService, UserMapper userMapper) {
                this.userService = userService;
                this.accountService = accountService;
                this.userMapper = userMapper;
        }

        @Override
        public void run(String... args) throws ParseException {
                User bankOwner = new User();
                bankOwner.setFirstName("WAVR");
                bankOwner.setLastName("BANKING");
                bankOwner.setEmail("wavr@bank.com");
                bankOwner.setPassword("11111");
                bankOwner.setBsn("832698734");
                bankOwner.setDateOfBirth(LocalDate.of(2004, 3, 23));
                bankOwner.setRole(UserType.EMPLOYEE);
                UserDTO bankOwnerDTO = userService.register(userMapper.userToRegisterDTO(bankOwner));
                bankOwnerDTO.setDayLimit(5000000);
                bankOwnerDTO.setTransactionLimit(5000); // 5000 limit for ATM
                userService.update(bankOwnerDTO.getId(), bankOwnerDTO);

                User employee = new User();
                employee.setFirstName("Root");
                employee.setLastName("Admin");
                employee.setEmail("root@gmail.com");
                employee.setPassword("11111");
                employee.setBsn("832698734");
                employee.setDateOfBirth(LocalDate.of(2004, 3, 23));
                employee.setRole(UserType.EMPLOYEE);
                UserDTO employeeDTO = userService.register(userMapper.userToRegisterDTO(employee));
                employeeDTO.setDayLimit(10000);
                employeeDTO.setTransactionLimit(5000);
                userService.update(employeeDTO.getId(), employeeDTO);

                User customer = new User();
                customer.setFirstName("Test");
                customer.setLastName("Customer");
                customer.setEmail("test@gmail.com");
                customer.setPassword("0000");
                customer.setBsn("832698724");
                customer.setDateOfBirth(LocalDate.of(2004, 3, 23));
                customer.setRole(UserType.CUSTOMER);
                UserDTO customerDto = userService.register(userMapper.userToRegisterDTO(customer));
                customerDto.setDayLimit(1000);
                customerDto.setTransactionLimit(500);
                userService.update(customerDto.getId(), customerDto);

                AccountDTO account1 = new AccountDTO();
                account1.setIban("NL01ABNA1032456789");
                account1.setBalance(220);
                account1.setTypeOfAccount(AccountType.CURRENT);
                account1.setUserId(employeeDTO.getId());
                account1.setDateOfOpening(LocalDate.now());
                account1.setAbsoluteLimit(0);
                account1.setActive(true);

                accountService.createAccount(account1);

                AccountDTO account2 = new AccountDTO();
                account2.setIban("NL01ABNA0123456789");
                account2.setBalance(110);
                account2.setTypeOfAccount(AccountType.CURRENT);
                account2.setUserId(customerDto.getId());
                account2.setDateOfOpening(LocalDate.now());
                account2.setAbsoluteLimit(0);
                account2.setActive(true);

                accountService.createAccount(account2);

                accountService.createBankAccount(bankOwnerDTO.getId());
        }
}