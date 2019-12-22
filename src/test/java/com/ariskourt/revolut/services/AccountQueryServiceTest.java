package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountQueryServiceTest {

    private static final String ID_1 = UUID.randomUUID().toString();
    private static final String ID_2 = UUID.randomUUID().toString();
    private static final String ID_3 = UUID.randomUUID().toString();

    @Mock private QueryRunnerService queryRunnerService;
    @Mock private QueryRunner queryRunner;

    private AccountQueryService queryService;
    private BankAccount account;
    private List<BankAccount> accountList;

    @BeforeEach
    void setUp() {
        queryService = new AccountQueryServiceImpl(queryRunnerService);
        account = getAccount(ID_1);
        accountList = List.of(getAccount(ID_2), getAccount(ID_3));
    }

    @Test
    @DisplayName("When id is matched at the database, account is returned")
    public void getAccountBy_WhenIdIsMatched_AccountObjectIsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), eq(ID_1))).thenReturn(account);
        BankAccount retrieved = queryService.getAccountBy(ID_1);
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID_1));
        assertNotNull(retrieved);
        assertEquals(account.getId(), retrieved.getId());
        assertEquals(account.getAccountHolder(), retrieved.getAccountHolder());
        assertEquals(account.getAccountBalance(), retrieved.getAccountBalance());
        assertEquals(account.getCreatedAt(), retrieved.getCreatedAt());
        assertEquals(account.getVersion(), retrieved.getVersion());
    }

    @Test
    @DisplayName("When id is not matched at the database, null is returned")
    public void getAccountBy_WhenIdIsNotMatched_NullIsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), eq(ID_1))).thenReturn(null);
        BankAccount retrieved = queryService.getAccountBy(ID_1);
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID_1));
        assertNull(retrieved);
    }

    @Test
    @DisplayName("When records are present, an account list is returned")
    public void getAccounts_WhenRecordsArePresent_AccountListGetsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanListHandler.class))).thenReturn(accountList);
        List<BankAccount> accounts = queryService.getAccounts();
        verify(queryRunner).query(anyString(), any(BeanListHandler.class));
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    @DisplayName("When records are not present, an empty account list is returned")
    public void getAccounts_WhenRecordsNotPresent_EmptyListGetsReturned() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanListHandler.class))).thenReturn(Collections.emptyList());
        List<BankAccount> accounts = queryService.getAccounts();
        verify(queryRunner).query(anyString(), any(BeanListHandler.class));
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    @DisplayName("When an SQLExceptions is thrown attempting to list multiple values, it gets handled correctly")
    public void getAccounts_WhenSqlExceptionOccurs_ExceptionsIsHandledAndRethrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanListHandler.class))).thenThrow(new SQLException("Invalid SQL statement"));
        assertThrows(DataAccessException.class, () -> queryService.getAccounts());
        verify(queryRunner).query(anyString(), any(BeanListHandler.class));
    }

    @Test
    @DisplayName("When SQLException is thrown, exception is handled and rethrown")
    public void getBankAccount_WhenSqlExceptionIsThrown_ExceptionIsCaughtAndRethrown() throws SQLException {
        when(queryRunnerService.get()).thenReturn(queryRunner);
        when(queryRunner.query(anyString(), any(BeanHandler.class), anyString())).thenThrow(new SQLException("Invalid SQL statement"));
        assertThrows(DataAccessException.class, () -> queryService.getAccountBy(ID_1));
        verify(queryRunner).query(anyString(), any(BeanHandler.class), eq(ID_1));
    }

    private BankAccount getAccount(String id) {
        var account = new BankAccount();
        account.setId(id);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(1000L);
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}