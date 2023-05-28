package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a.iban FROM Account a JOIN a.user u WHERE CONCAT(u.firstName, ' ', u.lastName) = :username")
    List<String>  findIbanByUsername(@Param("username") String customerName);

    Account findByIban(String iban);

    float findBalanceByIban(String iban);

}
