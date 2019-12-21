package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.utils.Pair;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountValidationServiceTest {

    private static final String FROM_UUID = "7196c512-6fa7-4d97-a27a-cdced71d4ca3";
    private static final String TO_UUID = "1e1229d3-0d76-4f52-9fcb-903d577211e8";

    @Mock
    private QueryRunnerService queryRunnerService;

    @Mock
    private QueryRunner runner;

    private AccountValidationService service;
    private AccountTransferRequest request;
    private BankAccount fromAccount;
    private BankAccount toAccount;

    @BeforeEach
    void setUp() {
        service = new AccountValidationService(queryRunnerService);
        request = createRequest(1000L);
        fromAccount = createAccount(1000L, FROM_UUID);
        toAccount = createAccount(2000L, TO_UUID);
    }

    @Test
    @DisplayName("Test correct validation when both accounts are present")
    public void validateRequest_WhenBothAccountsPresentAndNotIdentical_validationPasses() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(fromAccount);
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(toAccount);
        try {
            Pair<BankAccount, BankAccount> accountPair = service.validateAndGetAccounts(request);
            assertAll(() -> {
                assertEquals(fromAccount, accountPair.getLeft());
                assertEquals(toAccount, accountPair.getRight());
            });
        } catch (AbstractApplicationException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Test correct validation when both account are not present")
    public void validateRequest_WhenBothAccountsNotPresent_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(null);
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(null);
        assertThrows(BankAccountNotFoundException.class, () -> service.validateAndGetAccounts(request));
    }

    @Test
    @DisplayName("Test correct validation when fromAccount is not present")
    public void validateRequest_WhenFromAccountNotPresent_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(null);
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(toAccount);
        assertThrows(BankAccountNotFoundException.class, () -> service.validateAndGetAccounts(request));
    }

    @Test
    @DisplayName("Test correct validation when toAccount is not present")
    public void validateRequest_WhenToAccountNotPresent_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(fromAccount);
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(null);
        assertThrows(BankAccountNotFoundException.class, () -> service.validateAndGetAccounts(request));
    }

    @Test
    @DisplayName("Test correct validation when both accounts are present and the same")
    public void validateRequest_WhenBothAccountsPresentAndIdentical_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), anyString())).thenReturn(fromAccount);
        assertThrows(SameAccountTransferException.class, () -> service.validateAndGetAccounts(request));
    }

    @Test
    @DisplayName("Test correct validation when fromAccount is present but balance is equal or lower tha zero")
    public void validateRequest_WhenFromAccountBalanceIsUnderOrEqualToZero_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(createAccount(0L, FROM_UUID));
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(toAccount);
        assertThrows(InsufficientBalanceException.class, () -> service.validateAndGetAccounts(request));
    }

    @Test
    @DisplayName("Test correct validation when requested transfer amount is over fromAccount's balance")
    public void validateRequest_WhenFromAccountBalanceLowerThanTransferAmount_validationFailsExceptionIsThrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(runner);
        when(runner.query(anyString(), any(BeanHandler.class), eq(FROM_UUID))).thenReturn(createAccount(0L, FROM_UUID));
        when(runner.query(anyString(), any(BeanHandler.class), eq(TO_UUID))).thenReturn(toAccount);
        assertThrows(InsufficientBalanceException.class, () -> service.validateAndGetAccounts(createRequest(10000L)));
    }

    private BankAccount createAccount(Long balance, String id) {
        BankAccount account = new BankAccount();
        account.setId(id);
        account.setAccountBalance(balance);
        account.setAccountHolder("John Doe");
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

    private AccountTransferRequest createRequest(Long amount) {
        AccountTransferRequest request = new AccountTransferRequest();
        request.setFromAccount(FROM_UUID);
        request.setToAccount(TO_UUID);
        request.setAmount(amount);
        return request;
    }

}