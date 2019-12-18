package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.api.ErrorResource;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<AbstractServiceException> {

    @Override
    public Response toResponse(AbstractServiceException e) {
	return Response.status(e.getStatus())
	    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
	    .entity(new ErrorResource(e.getStatus().getStatusCode(), e.getMessage()))
	    .build();
    }

}
