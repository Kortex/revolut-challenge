package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.ws.rs.core.Response;

public class BankAccountNotFoundException extends AbstractApplicationException {

    public BankAccountNotFoundException(String message, String... args) {
	super(message, ApplicationError.ACCOUNT_NOT_FOUND, args);
    }

    @Override
    public Response.Status getStatus() {
	return Response.Status.NOT_FOUND;
    }

}
