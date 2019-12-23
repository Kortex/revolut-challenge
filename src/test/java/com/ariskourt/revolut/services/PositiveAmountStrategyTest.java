package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PositiveAmountStrategyTest {

    private static final Long AMOUNT = 1000L;
    private static final Long FROM_START = 5000L;
    private static final Long TO_START = 2000L;

    private BankAccount from;
    private BankAccount to;
    private PositiveAmountStrategy strategy;

    @BeforeEach
    void setUp() {
        from = createAccount(5000L);
        to = createAccount(2000L);
        strategy = new PositiveAmountStrategy(AMOUNT);
    }

    @Test
    @DisplayName("Calling accept for positive amount, results in correct amount being transferred")
    public void accept_WhenCalled_CorrectAmountIsTransferred() {
        strategy.accept(from, to);
        assertEquals(FROM_START - AMOUNT, from.getAccountBalance());
        assertEquals(TO_START + AMOUNT, to.getAccountBalance());
    }

    private BankAccount createAccount(Long startBalance) {
        var account = new BankAccount();
        account.setAccountBalance(startBalance);
        account.setAccountHolder("John Doe");
        account.setId(UUID.randomUUID().toString());
        account.setVersion(1);
        account.setCreatedAt(new Date());
        return account;
    }

}