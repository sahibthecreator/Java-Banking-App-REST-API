package com.bank.app.restapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void newAccountShouldNotBeNull(){
        Account account = new Account();
        assertNotNull(account);
    }

    @Test
    void accountUserFieldIsNotNull(){
        Account account = new Account();
        account.setUser(new User());
        assertNotNull(account.getUser());
    }

    @Test
    // ensure user field is properly associated with account
    void accountUserFieldIsAssociatedWithAccount(){
        Account account = new Account();
        User user = new User();
        account.setUser(user);
        assertEquals(user, account.getUser());
    }

    @Test
    // test the account balance field
    void accountBalanceFieldIsNotNull(){
        Account account = new Account();
        account.setBalance(0.0f);
        assertNotNull(account.getBalance());
    }

    @Test
    void accountUpdatedBalanceMatchesRetrievedBalance(){
        float updatedBalance = 100.0f;
        Account account = new Account();
        account.setBalance(updatedBalance);

        float retrievedBalance =  account.getBalance();

        assertEquals(updatedBalance, retrievedBalance);
    }

}