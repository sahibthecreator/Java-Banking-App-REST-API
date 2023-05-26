package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.AccountDTO;
import com.bank.app.restapi.model.Account;
import org.modelmapper.ModelMapper;

public class AccountMapper {

    private ModelMapper mapper;

    public AccountMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = mapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    public Account toEntity(AccountDTO accountDTO) throws IllegalArgumentException {
        Account account = mapper.map(accountDTO, Account.class);
        return account;
    }

}
