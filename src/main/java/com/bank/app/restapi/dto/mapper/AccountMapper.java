package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.model.Account;
import org.modelmapper.ModelMapper;

import java.text.ParseException;

public class AccountMapper {

    private ModelMapper mapper;

    public AccountMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AccountDTO toDTO(Account a) {
        return mapper.map(a, AccountDTO.class);
    }

    public Account toEntity(AccountDTO a) throws ParseException {
        Account account = mapper.map(a, Account.class);
        // set custom vars here

        if (account.getId() != null) {
            // code here...
        }
        return account;
    }
}
