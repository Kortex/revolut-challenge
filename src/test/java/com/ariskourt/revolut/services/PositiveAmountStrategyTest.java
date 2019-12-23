package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PositiveAmountStrategyTest {

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(1000.0);
    private static final BigDecimal FROM_START = BigDecimal.valueOf(5000.00);
    private static final BigDecimal TO_START = BigDecimal.valueOf(2000.0);

    private BankAccount from;
    private BankAccount to;
    private PositiveAmountStrategy strategy;

    @BeforeEach
    void setUp() {
        from = createAccount(FROM_START);
        to = createAccount(TO_START);
        strategy = new PositiveAmountStrategy(AMOUNT);
    }

    @Test
    @DisplayName("Calling accept for positive amount, results in correct amount being transferred")
    public void accept_WhenCalled_CorrectAmountIsTransferred() {
        strategy.accept(from, to);
        assertEquals(FROM_START.subtract(AMOUNT), from.getAccountBalance());
        assertEquals(TO_START.add(AMOUNT), to.getAccountBalance());
    }

    private BankAccount createAccount(BigDecimal startBalance) {
        var account = new BankAccount();
        account.setAccountBalance(startBalance);
        account.setAccountHolder("John Doe");
        account.setId(UUID.randomUUID().toString());
        account.setVersion(1);
        account.setCreatedAt(new Date());
        return account;
    }

}