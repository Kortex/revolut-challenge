package com.ariskourt.revolut.exceptions;

import javax.ws.rs.core.Response;

public abstract class AbstractServiceException extends RuntimeException {

    AbstractServiceException(String message) {
        super(message);
    }

    abstract Response.Status getStatus();

}
