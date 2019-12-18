package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.api.ErrorResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ApplicationExceptionMapperTest {

    private static final String EXCEPTION_MESSAGE = "Some exception message";

    private ApplicationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
	mapper = new ApplicationExceptionMapper();
    }

    @Test
    public void ToResponse_WhenExceptionIsPassed_ResponseIsCreatedAndSerialized() {
	SomeException exception = new SomeException(EXCEPTION_MESSAGE);
	Response response = mapper.toResponse(exception);
	assertNotNull(response);
	ErrorResource resource = (ErrorResource) response.getEntity();
	assertAll(() -> {
	    assertNotNull(resource.getMessage());
	    assertNotNull(resource.getCode());
	    assertEquals(EXCEPTION_MESSAGE, resource.getMessage());
	    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), (int) resource.getCode());
	});
    }

    private static class SomeException extends AbstractServiceException {
        SomeException(String message) {
            super(message);
	}

	@Override
	Response.Status getStatus() { return Response.Status.NOT_FOUND; }
    }

}