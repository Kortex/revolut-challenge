package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransferValidationServiceTest {

    private TransferValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new TransferValidationServiceImpl();
    }

    @Test
    @DisplayName("When no from and to account are found validation fails")
    public void validateTransferDetails_WhenBothAccountsNotPresent_ExceptionIsThrown() {
        Pair<BankAccount, BankAccount> nullPair = Pair.of(null, null);
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(nullPair, 1000L));
    }

    @Test
    @DisplayName("When no from account is found validation fails")
    public void validateTransferDetails_WhenFromAccountNotPresent_ExceptionIsThrown() {
        Pair<BankAccount, BankAccount> nullFromPair = Pair.of(null, createAccount(1000L));
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(nullFromPair, 1000L));
    }

    @Test
    @DisplayName("When no to account is found validation fails")
    public void validateTransferDetails_WhenToAccountNotPresent_ExceptionIsThrown() {
        Pair<BankAccount, BankAccount> nullToPair = Pair.of(createAccount(1000L), null);
        assertThrows(BankAccountNotFoundException.class, () -> validationService.validateTransferDetails(nullToPair, 1000L));
    }

    @Test
    @DisplayName("When same account transfer is attempted, validation fails")
    public void validateTransferDetails_WhenAccountsAreTheSame_ExceptionIsThrown() {
        var createdAt = new Date();
        var id = UUID.randomUUID().toString();
        var from = createAccount(id, 1000L, createdAt);
        var to = createAccount(id, 1000L, createdAt);
        Pair<BankAccount, BankAccount> samePair = Pair.of(from, to);
        assertThrows(SameAccountTransferException.class, () -> validationService.validateTransferDetails(samePair, 1000L));
    }

    @Test
    @DisplayName("When from account has no balance left, validation fails")
    public void validateTransferDetails_WhenFromAccountHasNoBalanceLeft_ExceptionIsThrown() {
        var from = createAccount(0L);
        var to = createAccount(1000L);
        Pair<BankAccount, BankAccount> noBalancePair = Pair.of(from, to);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(noBalancePair, 1000L));
    }

    @Test
    @DisplayName("When from account has negative balance, validation fails")
    public void validateTransferDetails_WhenAccountHasNegativeBalance_ExceptionIsThrown() {
        var from = createAccount(-100L);
        var to = createAccount(1000L);
        Pair<BankAccount, BankAccount> noBalancePair = Pair.of(from, to);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(noBalancePair, 1000L));
    }

    @Test
    @DisplayName("When from account has insufficient balance, validation fails")
    public void validateTransferDetails_WhenFromAccountHasInsufficientBalance_ExceptionIsThrown() {
        var from = createAccount(500L);
        var to = createAccount(1000L);
        Pair<BankAccount, BankAccount> lowBalancePair = Pair.of(from, to);
        assertThrows(InsufficientBalanceException.class, () -> validationService.validateTransferDetails(lowBalancePair, 2000L));
    }

    @Test
    @DisplayName("When both accounts are fine, validation succeeds")
    public void validateTransferDetails_WhenBothAccountsAreEligibleForTransfer_NoExceptionIsThrown() {
        var from = createAccount(5000L);
        var to = createAccount(1000L);
        Pair<BankAccount, BankAccount> correctAccountPair = Pair.of(from, to);
        try {
            validationService.validateTransferDetails(correctAccountPair, 1000L);
        } catch (Exception e) {
            fail();
        }
    }

    private BankAccount createAccount(String id, Long balance, Date createdAt) {
        var account = new BankAccount();
        account.setId(id);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(balance);
        account.setCreatedAt(createdAt);
        account.setVersion(1);
        return account;
    }

    private BankAccount createAccount(Long balance) {
        var account = new BankAccount();
        account.setId(UUID.randomUUID().toString());
        account.setAccountHolder("John Doe");
        account.setAccountBalance(balance);
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}