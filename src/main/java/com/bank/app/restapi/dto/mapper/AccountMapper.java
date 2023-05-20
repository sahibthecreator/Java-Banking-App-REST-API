package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.model.Account;
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

     public boolean isValidDTO(AccountDTO accountDTO) {
         boolean ibanIsValid = accountDTO.getIban() != null && !accountDTO.getIban().isEmpty();
         boolean balanceIsValid = accountDTO.getBalance() >= 0.0;
         boolean dateOfOpeningIsValid = accountDTO.getDateOfOpening() != null && !accountDTO.getDateOfOpening().isBefore(LocalDate.now());
         boolean absoluteLimitIsValid = accountDTO.getAbsoluteLimit() >= 0.0;

         return ibanIsValid && balanceIsValid && dateOfOpeningIsValid && absoluteLimitIsValid;
     }
}
