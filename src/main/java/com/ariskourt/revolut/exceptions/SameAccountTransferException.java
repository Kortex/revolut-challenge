package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.ws.rs.core.Response;

public class SameAccountTransferException extends AbstractApplicationException {

    public SameAccountTransferException(String message, String... args) {
        super(message, ApplicationError.SAME_ACCOUNT_TRANSFER, args);
    }

    @Override
    public Response.Status getStatus() {
	return Response.Status.BAD_REQUEST;
    }

}
