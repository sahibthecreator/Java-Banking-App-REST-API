package com.bank.app.restapi.dto.mapper;

import org.modelmapper.ModelMapper;

import com.bank.app.restapi.dto.AccountRequestDTO;
import com.bank.app.restapi.model.AccountRequest;


public class AccountRequestMapper {

    private ModelMapper mapper;

    public AccountRequestMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AccountRequestDTO toDTO(AccountRequest request) {
        AccountRequestDTO accountRequestDTO = mapper.map(request, AccountRequestDTO.class);
        return accountRequestDTO;
    }

    public AccountRequest toEntity(AccountRequestDTO accountRequestDTO) throws IllegalArgumentException {
        AccountRequest accountRequest = mapper.map(accountRequestDTO, AccountRequest.class);
        return accountRequest;
    }
}
