package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class TransferValidationServiceTest {

    private TransferValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new TransferValidationServiceImpl();
    }

    @Test
    @DisplayName("When no from and to account are found validation fails")
    public void validateTransferDetails_WhenBothAccountsNotPresent_ExceptionIsThrown() {
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(null, null, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When no from account is found validation fails")
    public void validateTransferDetails_WhenFromAccountNotPresent_ExceptionIsThrown() {
        var from = createAccount(1000.0);
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(from, null, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When no to account is found validation fails")
    public void validateTransferDetails_WhenToAccountNotPresent_ExceptionIsThrown() {
        var from = createAccount(1000.0);
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(from, null, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When same account transfer is attempted, validation fails")
    public void validateTransferDetails_WhenAccountsAreTheSame_ExceptionIsThrown() {
        var createdAt = new Date();
        var id = UUID.randomUUID().toString();
        var from = createAccount(id, 1000.0, createdAt);
        var to = createAccount(id, 1000.0, createdAt);
        assertThrows(SameAccountTransferException.class, () -> validationService.validateTransferDetails(from, to, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When from account has no balance left, validation fails")
    public void validateTransferDetails_WhenFromAccountHasNoBalanceLeft_ExceptionIsThrown() {
        var from = createAccount(0.0);
        var to = createAccount(1000.0);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(from, to, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When from account has negative balance, validation fails")
    public void validateTransferDetails_WhenAccountHasNegativeBalance_ExceptionIsThrown() {
        var from = createAccount(-100.0);
        var to = createAccount(1000.0);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(from, to, BigDecimal.valueOf(1000.0)));
    }

    @Test
    @DisplayName("When from account has insufficient balance, validation fails")
    public void validateTransferDetails_WhenFromAccountHasInsufficientBalance_ExceptionIsThrown() {
        var from = createAccount(500.0);
        var to = createAccount(1000.0);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(from, to, BigDecimal.valueOf(2000.0)));
    }

    @Test
    @DisplayName("When from account has insufficient balance and transfer amount is negative, validation fails")
    public void validateTransferDetails_WhenFromAccountHasInsufficientBalanceNegativeTransferAmount_ExceptionIsThrown() {
        var from = createAccount(500.0);
        var to = createAccount(1000.0);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(from, to, BigDecimal.valueOf(-2000.0)));
    }

    @Test
    @DisplayName("When both accounts are fine, validation succeeds")
    public void validateTransferDetails_WhenBothAccountsAreEligibleForTransfer_NoExceptionIsThrown() {
        var from = createAccount(5000.0);
        var to = createAccount(1000.0);
        try {
            validationService.validateTransferDetails(from, to, BigDecimal.valueOf(1000.0));
        } catch (Exception e) {
            fail();
        }
    }

    private BankAccount createAccount(String id, Double balance, Date createdAt) {
        var account = new BankAccount();
        account.setId(id);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(BigDecimal.valueOf(balance));
        account.setCreatedAt(createdAt);
        account.setVersion(1);
        return account;
    }

    private BankAccount createAccount(Double balance) {
        var account = new BankAccount();
        account.setId(UUID.randomUUID().toString());
        account.setAccountHolder("John Doe");
        account.setAccountBalance(BigDecimal.valueOf(balance));
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}