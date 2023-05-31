package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.AccountRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, UUID> {

}
