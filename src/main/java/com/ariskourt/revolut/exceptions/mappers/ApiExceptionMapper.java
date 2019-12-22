package com.ariskourt.revolut.exceptions.mappers;

import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<AbstractApplicationException> {

    @Override
    public Response toResponse(AbstractApplicationException e) {
	return Response.status(e.getStatus())
	    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
	    .entity(new ErrorResponse(e.getErrorCode(), e.getMessage()))
	    .build();
    }

}
