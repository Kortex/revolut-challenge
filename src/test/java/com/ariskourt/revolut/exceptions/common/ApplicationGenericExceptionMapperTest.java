package com.ariskourt.revolut.exceptions.common;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.mappers.ApplicationGenericExceptionMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationGenericExceptionMapperTest {

    Exception exception;
    ApplicationGenericExceptionMapper genericExceptionMapper;

    @BeforeEach
    void setUp() {
        exception = new Exception("Some exception message");
        genericExceptionMapper = new ApplicationGenericExceptionMapper();
    }

    @Test
    @DisplayName("Any exception gets correctly translated to a response")
    public void toResponse_WhenAnyExceptionIsPassedIn_ExceptionIsTranslatedToResponse() {
        Response response = genericExceptionMapper.toResponse(exception);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        var entity = (ErrorResponse) response.getEntity();
        assertNotNull(entity.getMessage());
        assertNotNull(entity.getErrorCode());
        assertNotNull(entity.getStacktrace());
    }

    @Test
    @DisplayName("When throwable's message is null (e.g NPE) the default one is returned")
    public void toResponse_WhenThrowableMessageNull_DefaultMessageIsReturnedInstead() {
        Exception exception = new Exception();
        Response response = genericExceptionMapper.toResponse(exception);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        var entity = (ErrorResponse) response.getEntity();
        assertNotNull(entity.getMessage());
        assertNotNull(entity.getErrorCode());
        assertNotNull(entity.getStacktrace());
        assertThat(entity.getMessage(), Matchers.is(ApplicationGenericExceptionMapper.UNKNOWN_ERROR_MESSAGE));
    }

}