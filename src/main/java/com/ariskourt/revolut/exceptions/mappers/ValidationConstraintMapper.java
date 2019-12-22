package com.ariskourt.revolut.exceptions.mappers;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationConstraintMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
	return Response.status(Response.Status.BAD_REQUEST)
	    .entity(new ErrorResponse(ApplicationError.INVALID_PAYLOAD_ERROR.getCode(),
		violationsToMessage(e),
		null))
	    .build();
    }

    private String violationsToMessage(ConstraintViolationException e) {
	StringBuilder messageBuilder = new StringBuilder();
	e.getConstraintViolations()
	    .stream()
	    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage() + "\n")
	    .forEach(messageBuilder::append);
	return messageBuilder.toString();
    }

}
