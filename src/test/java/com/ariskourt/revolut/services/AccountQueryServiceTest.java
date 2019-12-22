package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountQueryServiceTest {

    private static final String ID = UUID.randomUUID().toString();

    @Mock private QueryRunnerService queryRunnerService;
    @Mock private QueryRunner queryRunner;

    private AccountQueryService queryService;
    private BankAccount account;

    @BeforeEach
    void setUp() {
        queryService = new AccountQueryServiceImpl(queryRunnerService);
        account = getAccount();
    }

    @Test
    @DisplayName("When id is matched at the database, account is returned")
    public void getBankAccount_WhenIdIsMatched_AccountObjectIsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), eq(ID))).thenReturn(account);
        BankAccount retrieved = queryService.getBankAccount(ID);
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID));
        assertNotNull(retrieved);
        assertEquals(account.getId(), retrieved.getId());
        assertEquals(account.getAccountHolder(), retrieved.getAccountHolder());
        assertEquals(account.getAccountBalance(), retrieved.getAccountBalance());
        assertEquals(account.getCreatedAt(), retrieved.getCreatedAt());
        assertEquals(account.getVersion(), retrieved.getVersion());
    }

    @Test
    @DisplayName("When id is not matched at the database, null is returned")
    public void getBankAccount_WhenIdIsNotMatched_NullIsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), eq(ID))).thenReturn(null);
        BankAccount retrieved = queryService.getBankAccount(ID);
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID));
        assertNull(retrieved);
    }

    @Test
    @DisplayName("When SQLException is thrown, exception is handled and rethrown")
    public void getBankAccount_WhenSqlExceptionIsThrown_ExceptionIsCaughtAndRethrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), anyString())).thenThrow(new SQLException("Invalid SQL statement"));
        assertThrows(DataAccessException.class, () -> queryService.getBankAccount(ID));
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID));
    }

    private BankAccount getAccount() {
        var account = new BankAccount();
        account.setId(ID);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(1000L);
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}