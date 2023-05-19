package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.User;
import org.modelmapper.ModelMapper;

import java.text.ParseException;
import java.time.LocalDate;

public class AccountMapper {

    private ModelMapper mapper;

    public AccountMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = mapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    public Account toEntity(AccountDTO accountDTO) throws ParseException {
        Account account = mapper.map(accountDTO, Account.class);
        // set custom vars here

        // if (accountDTO.getId() != null) {
        //     // get user by id
        //     // set other fields only retrievable from service here
        // }
        return account;
    }

    // public boolean isValidDTO(UserDTO userDTO) {
    //     boolean firstNameIsValid = userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty();
    //     boolean lastNameIsValid = userDTO.getLastName() != null && !userDTO.getLastName().isEmpty();
    //     boolean emailIsValid = userDTO.getEmail() != null && !userDTO.getEmail().isEmpty();
    //     boolean passwordIsValid = userDTO.getPassword() != null && !userDTO.getPassword().isEmpty();
    //     boolean bsnIsValid = userDTO.getBsn() != null && !userDTO.getBsn().isEmpty();
    //     boolean dateOfBirthIsValid = userDTO.getDateOfBirth() != null
    //             && userDTO.getDateOfBirth().isBefore(LocalDate.now());

    //     // !!!!!!ONLY FOR TESTING PHASE ONLY!!!!!
    //     // Ye, This looks horrible XD - Yvan
    //     boolean roleIsValid = userDTO.getRole() != null;

    //     return firstNameIsValid && lastNameIsValid && emailIsValid && passwordIsValid && bsnIsValid
    //             && dateOfBirthIsValid && roleIsValid;
    // }
}
