package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUpdateServiceTest {

    private static final Long BALANCE = 1000L;
    private static final String ID = UUID.randomUUID().toString();

    @Mock private QueryRunnerService queryRunnerService;
    @Mock private QueryRunner queryRunner;

    private AccountUpdateService updateService;
    private BankAccount account;

    @BeforeEach
    void setUp() {
        updateService = new AccountUpdateServiceImpl(queryRunnerService);
        account = getAccount();
    }

    @Test
    @DisplayName("When correct object update occurs nothing is returned")
    public void updateAccount_WhenUpdateHappensWithNoIssues_NothingIsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.update(anyString(), eq(BALANCE), eq(ID), eq(ID))).thenReturn(1);
        updateService.updateAccount(account);
        verify(queryRunner).update(anyString(), eq(BALANCE), eq(ID), eq(ID));
    }

    @Test
    @DisplayName("When a SQLException occurs, exception is handled and rethrown")
    public void updateAccount_WhenSqlExceptionIsThrown_ExceptionIsHandledAndRethrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.update(anyString(), eq(BALANCE), eq(ID), eq(ID))).thenThrow(new SQLException("Invalid SQL statement"));
        assertThrows(DataAccessException.class, () -> updateService.updateAccount(account));
        verify(queryRunner).update(anyString(), eq(BALANCE), eq(ID), eq(ID));
    }

    private BankAccount getAccount() {
        var account = new BankAccount();
        account.setId(ID);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(BALANCE);
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}