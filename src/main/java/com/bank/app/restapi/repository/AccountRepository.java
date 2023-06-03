package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    @Query("SELECT a.iban FROM Account a JOIN a.user u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    List<String> findIbanByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    List<Account> findAccountsByUserId(@Param("userId") UUID userId);

    Account findByIban(String iban);

//    float findBalanceByIban(String iban);

}
