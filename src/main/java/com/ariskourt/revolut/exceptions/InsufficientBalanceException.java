package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.ws.rs.core.Response;

public class InsufficientBalanceException extends AbstractApplicationException {

    public InsufficientBalanceException(String message) {
        super(message, ApplicationError.INSUFFICIENT_ACCOUNT_BALANCE);
    }

    @Override
    public Response.Status getStatus() {
	return Response.Status.BAD_REQUEST;
    }

}
