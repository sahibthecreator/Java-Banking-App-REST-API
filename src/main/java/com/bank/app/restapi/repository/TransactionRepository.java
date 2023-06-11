package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//Extending with JpaSpecificationExecutor because I use Specifications to build my queries
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    //Finding transactions related with a user
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.iban IN " +
            "(SELECT DISTINCT a.iban FROM Account a WHERE a.user.id = :userId) " +
            "OR t.toAccount.iban IN " +
            "(SELECT DISTINCT a.iban FROM Account a WHERE a.user.id = :userId))")
    List<Transaction> findTransactionsByUserId(@Param("userId") UUID userId);

}
