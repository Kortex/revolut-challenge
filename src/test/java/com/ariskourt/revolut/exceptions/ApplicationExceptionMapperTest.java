package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.api.resources.ErrorResponse;
import com.ariskourt.revolut.exceptions.common.ApplicationExceptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.text.MessageFormat;

import static com.ariskourt.revolut.exceptions.common.ApplicationError.ACCOUNT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionMapperTest {

    private static final int ACCOUNT_ID = 1;
    private static final String MESSAGE = "No account found for id {1}";

    private ApplicationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
	mapper = new ApplicationExceptionMapper();
    }

    @Test
    public void ToResponse_WhenExceptionIsPassed_ResponseIsCreatedAndSerialized() {
        BankAccountNotFoundException exception = new BankAccountNotFoundException(MESSAGE, Integer.toString(ACCOUNT_ID));
	Response response = mapper.toResponse(exception);
	assertNotNull(response);
	ErrorResponse resource = (ErrorResponse) response.getEntity();
	assertAll(() -> {
	    assertNotNull(resource.getMessage());
	    assertNotNull(resource.getErrorCode());
	    assertEquals(MessageFormat.format(MESSAGE, ACCOUNT_ID), resource.getMessage());
	    assertEquals(ACCOUNT_NOT_FOUND.getErrorCode(), resource.getErrorCode());
	    assertEquals(exception.getStatus(), response.getStatusInfo());
	});
    }

}