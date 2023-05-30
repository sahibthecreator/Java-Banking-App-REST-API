package com.bank.app.restapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TransactionTest {

    @Mock
    private Account mockFromAccount;

    @Mock
    private Account mockToAccount;

    @Mock
    private User mockUser;

    private Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setFromAccount(mockFromAccount);
        transaction.setToAccount(mockToAccount);
        transaction.setAmount(100.0f);
        transaction.setTypeOfTransaction(TransactionType.DEPOSIT);
        transaction.setDateOfExecution(LocalDateTime.now());
        transaction.setPerformingUser(mockUser);
        transaction.setDescription("Test transaction");
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(transaction.getId(), transaction.getId());
        assertEquals(mockFromAccount, transaction.getFromAccount());
        assertEquals(mockToAccount, transaction.getToAccount());
        assertEquals(100.0f, transaction.getAmount());
        assertEquals(TransactionType.DEPOSIT, transaction.getTypeOfTransaction());
        assertEquals(transaction.getDateOfExecution(), transaction.getDateOfExecution());
        assertEquals(mockUser, transaction.getPerformingUser());
        assertEquals("Test transaction", transaction.getDescription());
    }

    //Not sure if it's needed but why not
    @Test
    public void testToString() {
        String expectedString = "Transaction(id=" + transaction.getId() +
                ", fromAccount=" + mockFromAccount.toString() +
                ", toAccount=" + mockToAccount.toString() +
                ", amount=100.0" +
                ", typeOfTransaction=DEPOSIT" +
                ", dateOfExecution=" + transaction.getDateOfExecution().toString() +
                ", performingUser=" + mockUser.toString() +
                ", description=Test transaction)";
        assertEquals(expectedString, transaction.toString());
    }
}