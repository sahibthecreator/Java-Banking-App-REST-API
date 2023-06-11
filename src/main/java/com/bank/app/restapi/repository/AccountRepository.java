package com.bank.app.restapi.repository;

import com.bank.app.restapi.dto.CustomerIbanDTO;
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
    @Query("SELECT new com.bank.app.restapi.dto.CustomerIbanDTO(u.firstName, u.lastName, a.iban) FROM Account a JOIN a.user u WHERE LOWER(u.firstName) LIKE %:firstName% AND LOWER(u.lastName) LIKE %:lastName%")
    List<CustomerIbanDTO> findIbanByFirstNameAndLastName(@Param("firstName") String firstName,
            @Param("lastName") String lastName);

    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    List<Account> findAccountsByUserId(@Param("userId") UUID userId);

    @Query("SELECT a.iban FROM Account a WHERE a.user.id = :userId")
    List<String> findIbanByUserId(@Param("userId") UUID userId);


    Account findByIban(String iban);

    // float findBalanceByIban(String iban);

}
