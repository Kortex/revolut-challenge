package com.ariskourt.revolut.exceptions.mappers;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.common.ApplicationError;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class ApplicationGenericExceptionMapper implements ExceptionMapper<Throwable> {

    public static final String UNKNOWN_ERROR_MESSAGE = "An unknown error has occurred!";

    @Override
    public Response toResponse(Throwable throwable) {
	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
	    .entity(getResponse(throwable))
	    .build();
    }

    private ErrorResponse getResponse(Throwable throwable) {
	return new ErrorResponse(ApplicationError.
	    INTERNAL_SERVER_ERROR.getCode(),
	    getThrowableMessageOrDefault(throwable), ExceptionUtils.getStackTrace(throwable));
    }

    private String getThrowableMessageOrDefault(Throwable throwable) {
	return Optional.ofNullable(throwable.getMessage())
	    .orElse(UNKNOWN_ERROR_MESSAGE);
    }

}
