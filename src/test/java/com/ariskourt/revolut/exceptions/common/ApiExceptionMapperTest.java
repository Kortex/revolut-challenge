package com.ariskourt.revolut.exceptions.common;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.DataAccessException;
import com.ariskourt.revolut.exceptions.mappers.ApiExceptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionMapperTest {

    private ApiExceptionMapper mapper;
    private DataAccessException exception;

    @BeforeEach
    void setUp() {
        mapper = new ApiExceptionMapper();
        exception = new DataAccessException(new SQLException("Invalid SQL statement"));
    }

    @Test
    @DisplayName("When provided with an exception, mapper translates it to response")
    public void toResponse_WhenExceptionIsCaught_ErrorResponseIsReturned() {
        Response response = mapper.toResponse(exception);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        ErrorResponse resp = (ErrorResponse) response.getEntity();
        assertEquals(exception.getErrorCode(), resp.getErrorCode());
        assertNull(resp.getStacktrace());
        assertNotNull(resp.getMessage());
    }

}