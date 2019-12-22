package com.ariskourt.revolut.exceptions.mappers;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.DataAccessException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.mappers.ApiExceptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionMapperTest {

    private ApiExceptionMapper mapper;
    private AbstractApplicationException exception;

    @BeforeEach
    void setUp() {
        mapper = new ApiExceptionMapper();
        exception = new DataAccessException(new SQLException("Invalid SQL statement"));
    }

    @Test
    @DisplayName("When provided with an exception, mapper translates it to response")
    public void toResponse_WhenExceptionIsCaught_ErrorResponseIsReturned() {
        Response response = mapper.toResponse(exception);
        assertEquals(exception.getStatus().getStatusCode(), response.getStatus());
        assertNotNull(response);
        assertNotNull(response.getEntity());
        ErrorResponse resp = (ErrorResponse) response.getEntity();
        assertEquals(exception.getErrorCode(), resp.getErrorCode());
        assertNull(resp.getStacktrace());
        assertNotNull(resp.getMessage());
    }

    @Test
    @DisplayName("Test verifying correct translation of BankAccountNotFoundException")
    public void toResponse_WhenBankAccountExceptionIsPassed_ErrorResponseIsCreatedCorrectly() {
        var message = "Account not found";
        exception = new BankAccountNotFoundException(message);
        Response response = mapper.toResponse(exception);
        assertEquals(exception.getStatus().getStatusCode(), response.getStatus());
        assertNotNull(response);
        assertNotNull(response.getEntity());
        ErrorResponse resp = (ErrorResponse) response.getEntity();
        assertEquals(exception.getErrorCode(), resp.getErrorCode());
        assertNull(resp.getStacktrace());
        assertNotNull(resp.getMessage());
        assertThat(resp.getMessage(), is(message));
    }

    @Test
    @DisplayName("Test verifying correct translation of InsufficientBalanceException")
    public void toResponse_WhenInsufficientBalanceExceptionsIsPassed_ErrorResponseIsCreatedCorrectly() {
        var message = "Insufficient account balance";
        exception = new InsufficientBalanceException(message);
        Response response = mapper.toResponse(exception);
        assertEquals(exception.getStatus().getStatusCode(), response.getStatus());
        assertNotNull(response);
        assertNotNull(response.getEntity());
        ErrorResponse resp = (ErrorResponse) response.getEntity();
        assertEquals(exception.getErrorCode(), resp.getErrorCode());
        assertNull(resp.getStacktrace());
        assertNotNull(resp.getMessage());
        assertThat(resp.getMessage(), is(message));
    }

    @Test
    @DisplayName("Test verifying correct translation of SameAccountTransferException")
    public void toResponse_WhenSameAccountTransferExceptionIsPassed_ErrorResponseIsCreatedCorrectly() {
        var message = "Transferring from and to the same is not allowed";
        exception = new SameAccountTransferException(message);
        Response response = mapper.toResponse(exception);
        assertEquals(exception.getStatus().getStatusCode(), response.getStatus());
        assertNotNull(response);
        assertNotNull(response.getEntity());
        ErrorResponse resp = (ErrorResponse) response.getEntity();
        assertEquals(exception.getErrorCode(), resp.getErrorCode());
        assertNull(resp.getStacktrace());
        assertNotNull(resp.getMessage());
        assertThat(resp.getMessage(), is(message));
    }

}