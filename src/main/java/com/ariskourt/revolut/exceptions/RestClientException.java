package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.ws.rs.core.Response;

public class RestClientException extends AbstractApplicationException {

    public RestClientException(String message) {
	super(message, ApplicationError.BAD_CURRENCY_API_CALL);
    }

    @Override
    public Response.Status getStatus() {
	return Response.Status.INTERNAL_SERVER_ERROR;
    }

}
