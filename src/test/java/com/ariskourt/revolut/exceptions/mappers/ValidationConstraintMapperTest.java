package com.ariskourt.revolut.exceptions.mappers;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.common.ApplicationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationConstraintMapperTest {

    private Validator validator;
    private ValidationConstraintMapper mapper;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ValidationConstraintMapper();
    }

    @Test
    @DisplayName("When constraint validation fails, exceptions is handled and translated to response")
    public void toResponse_WhenConstraintViolationExceptionOccurs_ResponseIsReturned() {
        var request = request();
        Set<ConstraintViolation<AccountTransferRequest>> violations = validator.validate(request());
        ConstraintViolationException exception = new ConstraintViolationException("Validating bean of type " + request.getClass().getCanonicalName() + " failed", violations);
        Response response = mapper.toResponse(exception);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        var entity = (ErrorResponse) response.getEntity();
        assertEquals(ApplicationError.INVALID_PAYLOAD_ERROR.getCode(), entity.getErrorCode());
        assertNull(entity.getStacktrace());
        assertNotNull(entity.getMessage());
    }

    private AccountTransferRequest request() {
        var request = new AccountTransferRequest();
        request.setAmount(null);
        request.setToAccount(null);
        request.setFromAccount(null);
        return request;
    }

}