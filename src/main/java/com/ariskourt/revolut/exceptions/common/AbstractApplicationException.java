package com.ariskourt.revolut.exceptions.common;

import lombok.Getter;

import javax.ws.rs.core.Response;

public abstract class AbstractApplicationException extends RuntimeException {

    @Getter
    private final Integer errorCode;

    public AbstractApplicationException(String message, ApplicationError error) {
        super(message);
        this.errorCode = error.getCode();
    }

    public abstract Response.Status getStatus();

}
